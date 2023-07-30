package com.bestbank.clientes.yanki.bussiness.messages.consumers;

import com.bestbank.clientes.yanki.bussiness.dto.transacciones.TransaccionBrokerRes;
import com.bestbank.commons.utils.JsonUtils;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransaccionesNotificarYankiConsumer {
  
 
/**
 * Recibe de Topico de Productos Registrados
 * 
 * @param productoBrokerRes
 */
  @KafkaListener(topics = "transacciones-notificar", groupId = "group_id")
  public void recibirTransaccionesNotificar(String jsonTransaccionesBrokerRes) {
    log.info("Recibiendo Transacciones Notificadas");
    TransaccionBrokerRes transaccionBrokerRes = 
        JsonUtils.jsonToObject(jsonTransaccionesBrokerRes, TransaccionBrokerRes.class);
    if (!Objects.isNull(transaccionBrokerRes)) {
      log.info(transaccionBrokerRes.toString());
    } else {
      log.error("Transaccion es Nulo");
    }
  }
  

}
