package com.bestbank.clientes.yanki.domain.model;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "yankiappctrl")
@Data
public class AppYankiCtrl {
  
  @Id
  private String id;
  
  private String numeroTelefono;
  
  private String imeiTelefono;
  
  private String emailPersona;
  
  private Integer indMonedero;
  
  private String codPersona;
  
  private String codProducto;
  
  private String codInstrumento;
  
  private Integer indEliminado;
  
  private Integer indStatus;
  
  private Date fechaCreacion;
  
  private Date fechaModificacion;

}
