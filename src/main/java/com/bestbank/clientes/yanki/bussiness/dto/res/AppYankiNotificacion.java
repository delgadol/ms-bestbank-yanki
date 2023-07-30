package com.bestbank.clientes.yanki.bussiness.dto.res;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * Notifacion al Front
 * Es el formato de respuesta al FRONT
 * 
 */
@Data
@AllArgsConstructor
public class AppYankiNotificacion {
  
  private String codProducto;
  
  private String mensaje;
  
  private Date fecNotificacion;

}
