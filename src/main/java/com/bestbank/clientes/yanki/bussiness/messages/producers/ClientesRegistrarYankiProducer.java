package com.bestbank.clientes.yanki.bussiness.messages.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.bestbank.clientes.yanki.bussiness.dto.clientes.ClienteBrokerReq;
import com.bestbank.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClientesRegistrarYankiProducer {
  
  private static final String KAFKA_TOPIC = "clientes-registrar";
  
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;
  
  public void enviarClientesResgistrar(ClienteBrokerReq cliente) {
    String jsonClienteBrokerReq = JsonUtils.objectToJson(cliente);
    log.info("cola clientes-registrar >>" + jsonClienteBrokerReq);
    this.kafkaTemplate.send(KAFKA_TOPIC, jsonClienteBrokerReq);
    
  }

}
