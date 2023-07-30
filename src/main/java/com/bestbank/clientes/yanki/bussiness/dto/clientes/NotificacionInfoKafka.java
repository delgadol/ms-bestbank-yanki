package com.bestbank.clientes.yanki.bussiness.dto.clientes;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificacionInfoKafka implements Serializable {
  
  private static final long serialVersionUID = -474882958904223201L;

  private String idNotificacion;
  
  private String descNotificacion;

}
