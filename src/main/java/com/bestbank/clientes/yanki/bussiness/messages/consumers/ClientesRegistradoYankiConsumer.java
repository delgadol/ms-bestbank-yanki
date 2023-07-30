package com.bestbank.clientes.yanki.bussiness.messages.consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.bestbank.clientes.yanki.bussiness.dto.clientes.ClienteBrokerRes;
import com.bestbank.clientes.yanki.bussiness.services.YankiOperationService;
import com.bestbank.commons.utils.JsonUtils;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClientesRegistradoYankiConsumer {
  
  @Autowired
  private YankiOperationService servOperaciones;
 
  
  @KafkaListener(topics = "clientes-registrado", groupId = "group_id")
  public void recibirClientesRegistrado(String clienteBrokerRes) {
    log.info("Recibiendo Cliente Registrado");
    ClienteBrokerRes clienteResp = JsonUtils.jsonToObject(clienteBrokerRes, ClienteBrokerRes.class);
    if (!Objects.isNull(clienteResp)) {
      servOperaciones
              .putClienteData(clienteResp)
              .subscribe(notif -> log.info(notif.toString()));
    } else {
      log.error("Cliente es Nulo");
    }
  }
  

}
