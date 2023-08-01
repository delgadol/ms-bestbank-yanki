package com.bestbank.clientes.yanki.bussiness.messages.producers;

import com.bestbank.clientes.yanki.bussiness.dto.transacciones.InfoTransacionBrokerReq;
import com.bestbank.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransaccionesRegistrarYankiProducer {
  
  private static final String KAFKA_ACT_REGISTRAR = "transacciones-registrar";
  
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;
  
  /**
   * Envia Datos al Topico de Registar Transacciones
   * 
   * @param cliente
   **/
  public void enviarTransaccionesRegistrar(InfoTransacionBrokerReq infoTransacionBrokerReq){
    String jsoninfoTransacionBrokerReq = JsonUtils.objectToJson(infoTransacionBrokerReq);
    if (jsoninfoTransacionBrokerReq.isBlank()) {
      log.info("Transaccion no valida");
      return;
    }
    log.info("cola transacciones - registrar >>" + infoTransacionBrokerReq.toString());
    this.kafkaTemplate.send(KAFKA_ACT_REGISTRAR, jsoninfoTransacionBrokerReq);
    
  }

}
