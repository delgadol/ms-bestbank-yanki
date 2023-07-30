package com.bestbank.clientes.yanki.bussiness.services.impl;

import com.bestbank.clientes.yanki.bussiness.dto.clientes.ClienteBrokerReq;
import com.bestbank.clientes.yanki.bussiness.dto.clientes.ClienteBrokerRes;
import com.bestbank.clientes.yanki.bussiness.dto.productos.ProductoBrokerReq;
import com.bestbank.clientes.yanki.bussiness.dto.productos.ProductoBrokerRes;
import com.bestbank.clientes.yanki.bussiness.dto.req.ClienteYankiReq;
import com.bestbank.clientes.yanki.bussiness.dto.req.TransacionYankiReq;
import com.bestbank.clientes.yanki.bussiness.dto.res.AppYankiNotificacion;
import com.bestbank.clientes.yanki.bussiness.dto.transacciones.InfoTransacionBrokerReq;
import com.bestbank.clientes.yanki.bussiness.messages.producers.ClientesRegistrarYankiProducer;
import com.bestbank.clientes.yanki.bussiness.messages.producers.ProductosRegistrarYankiProducer;
import com.bestbank.clientes.yanki.bussiness.messages.producers.TransaccionesRegistrarYankiProducer;
import com.bestbank.clientes.yanki.bussiness.services.YankiOperationService;
import com.bestbank.clientes.yanki.domain.model.AppYankiCtrl;
import com.bestbank.clientes.yanki.domain.repository.YankiCtrlRepository;
import com.bestbank.commons.tipos.TipoProducto;
import com.bestbank.commons.utils.BankFnUtils;
import com.bestbank.commons.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class YankiOperationServiceImpl implements YankiOperationService {

  @Autowired
  private YankiCtrlRepository servRepo;
  
  @Autowired
  private ClientesRegistrarYankiProducer servClientes;
  
  @Autowired
  private ProductosRegistrarYankiProducer servProductos;
  
  @Autowired
  private TransaccionesRegistrarYankiProducer servEnvTrans;
  
  
  @Override
  public Mono<AppYankiNotificacion> postCliente(ClienteYankiReq data) {
    AppYankiCtrl nuevoItem = ModelMapperUtils.map(data, AppYankiCtrl.class);
    // variables Adicionales -----
    nuevoItem.setCodPersona("");
    nuevoItem.setCodProducto("");
    nuevoItem.setCodInstrumento("");
    nuevoItem.setIndEliminado(0);
    nuevoItem.setIndStatus(2);
    nuevoItem.setFechaCreacion(BankFnUtils.getLegacyDateTimeNow());
    nuevoItem.setFechaModificacion(BankFnUtils.getLegacyDateTimeNow());
    // ---------------------------
    log.info("Recibido :: Data Cliente Nuevo");
    return servRepo.findFirstByNumeroTelefono(data.getNumeroTelefono())
        .flatMap(itemdb -> {
          log.info("Encontrado :: Cliente Existe");
          String msg = itemdb.getIndStatus().equals(0) ? "NORMAL" : "OBSERVADO";
          msg = itemdb.getIndEliminado().equals(1) ? "ELIMINADO" : msg;
          return Mono.just(new AppYankiNotificacion(
              itemdb.getId(), msg, itemdb.getFechaCreacion()));
        })
        .switchIfEmpty(
            servRepo.save(nuevoItem)
            .flatMap(item -> {
              log.info("Nuevo cliente :: envio cola");
              ClienteBrokerReq clienteBkReq = ModelMapperUtils.map(data, ClienteBrokerReq.class);
              clienteBkReq.setCodCtrlBroker(item.getId());
              servClientes.enviarClientesResgistrar(clienteBkReq);
              return Mono.just(new AppYankiNotificacion(
                  item.getId(), "PRODUCTO CREADO - STATUS 2", item.getFechaCreacion()));
            })
        );
  }

  @Override
  public Mono<AppYankiNotificacion> putClienteData(ClienteBrokerRes cliente) {
    return servRepo.findById(cliente.getCodCtrlBroker())
        .filter(itemf1 -> itemf1.getIndEliminado().equals(0))
        .flatMap(itemDb -> {
          AppYankiCtrl modItem = ModelMapperUtils.map(itemDb, AppYankiCtrl.class);
          modItem.setCodPersona(cliente.getId());
          modItem.setFechaModificacion(BankFnUtils.getLegacyDateTimeNow());
          modItem.setIndStatus(1);
          return servRepo.save(modItem)
            .flatMap(numItem -> {
              // enviamos a cola de productos ----------
              ProductoBrokerReq producto = new ProductoBrokerReq();
              producto.setCodCtrlBroker(numItem.getId());
              producto.setCodigoPersona(numItem.getCodPersona());
              producto.setTipoProducto(TipoProducto.CTYANKI);
              servProductos.enviarProductosRegistrar(producto);
              // ---------------------------------------
              return Mono.just(new AppYankiNotificacion(
                numItem.getId(), "PRODUCTO ACTUALIZADO - STATUS 1",
                numItem.getFechaModificacion()));
            });
        })
        .switchIfEmpty(
            Mono.just(new AppYankiNotificacion(
                cliente.getCodCtrlBroker(), "PRODUCTO NO ENCONTRADO",
                BankFnUtils.getLegacyDateTimeNow())
           )
        );        
  }

  @Override
  public Mono<AppYankiNotificacion> putProductoData(ProductoBrokerRes producto) {
    return servRepo.findById(producto.getCodCtrlBroker())
        .filter(itemf1 -> itemf1.getIndEliminado().equals(0))
        .flatMap(itemDb -> {
          AppYankiCtrl modItem = ModelMapperUtils.map(itemDb, AppYankiCtrl.class);
          modItem.setCodProducto(producto.getId());
          modItem.setFechaModificacion(BankFnUtils.getLegacyDateTimeNow());
          modItem.setIndStatus(0);
          return servRepo.save(modItem)
            .flatMap(numItem -> 
              Mono.just(new AppYankiNotificacion(
                numItem.getId(), "PRODUCTO ACTUALIZADO - STATUS 0",
                numItem.getFechaModificacion()))
            );
        })
        .switchIfEmpty(
            Mono.just(new AppYankiNotificacion(
                producto.getCodCtrlBroker(), "PRODUCTO NO ENCONTRADO",
                BankFnUtils.getLegacyDateTimeNow())
           )
        );  
  }

  @Override
  public Mono<AppYankiNotificacion> postOperacionData(TransacionYankiReq transaccion) {
    return servRepo.findFirstByNumeroTelefono(transaccion.getNumTelefono())
        .filter(itemf1 -> itemf1.getIndEliminado().equals(0))
        .flatMap(itemDb -> {
          InfoTransacionBrokerReq transReq = ModelMapperUtils.map(
              transaccion, InfoTransacionBrokerReq.class);
          transReq.setCodCtrlBroker(itemDb.getId());
          transReq.setIdProducto(itemDb.getCodProducto());
          transReq.setCodPersona(itemDb.getCodPersona());
          log.info("Enviado Transaccion a Cola");
          servEnvTrans.enviarTransaccionesRegistrar(transReq);
          return Mono.just(new AppYankiNotificacion(
                itemDb.getId(), "TRANSACCION ENVIADA - STATUS",
                BankFnUtils.getLegacyDateTimeNow()
              ));
        })
        .switchIfEmpty(
            Mono.just(new AppYankiNotificacion(
                transaccion.getNumTelefono(), "PRODUCTO NO ENCONTRADO",
                BankFnUtils.getLegacyDateTimeNow())
           )
        );  
  }


}
