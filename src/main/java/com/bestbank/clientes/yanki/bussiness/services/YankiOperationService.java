package com.bestbank.clientes.yanki.bussiness.services;


import com.bestbank.clientes.yanki.bussiness.dto.clientes.ClienteBrokerRes;
import com.bestbank.clientes.yanki.bussiness.dto.productos.ProductoBrokerRes;
import com.bestbank.clientes.yanki.bussiness.dto.req.ClienteYankiReq;
import com.bestbank.clientes.yanki.bussiness.dto.req.TransacionYankiReq;
import com.bestbank.clientes.yanki.bussiness.dto.res.AppYankiNotificacion;
import reactor.core.publisher.Mono;

public interface YankiOperationService {
  
  Mono<AppYankiNotificacion> postCliente(ClienteYankiReq data);
  
  Mono<AppYankiNotificacion> putClienteData(ClienteBrokerRes cliente);
  
  Mono<AppYankiNotificacion> putProductoData(ProductoBrokerRes producto);
  
  Mono<AppYankiNotificacion> postOperacionData(TransacionYankiReq transaccion);
  
}
