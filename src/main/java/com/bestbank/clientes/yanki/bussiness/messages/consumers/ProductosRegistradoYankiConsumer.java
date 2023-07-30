package com.bestbank.clientes.yanki.bussiness.messages.consumers;

import com.bestbank.clientes.yanki.bussiness.dto.productos.ProductoBrokerRes;
import com.bestbank.clientes.yanki.bussiness.services.YankiOperationService;
import com.bestbank.commons.utils.JsonUtils;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductosRegistradoYankiConsumer {
  
  @Autowired
  private YankiOperationService servOperaciones;
 
/**
 * Recibe de Topico de Productos Registrados
 * 
 * @param productoBrokerRes
 */
  @KafkaListener(topics = "productos-registrado", groupId = "group_id")
  public void recibirProdcutosRegistrado(String productoBrokerRes) {
    log.info("Recibiendo Producto Registrado");
    ProductoBrokerRes productoResp = 
        JsonUtils.jsonToObject(productoBrokerRes, ProductoBrokerRes.class);
    log.info(productoResp.toString());
    if (!Objects.isNull(productoResp)) {
      servOperaciones
              .putProductoData(productoResp)
              .subscribe(notif -> log.info(notif.toString()));
    } else {
      log.error("Pruducto es Nulo");
    }
  }
  

}
