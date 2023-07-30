package com.bestbank.clientes.yanki.bussiness.dto.req;


import com.bestbank.commons.tipos.TipoOperacion;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Clase que representa la solicitud de información de transacción.
 */

@Data
public class TransacionYankiReq {
  
  @NotEmpty(message = "Numero de Telefono Requerido")
  private String numTelefono;

  @NotNull(message = "Tipo Operacion Requerida")
  private TipoOperacion tipoOperacion;
  
  @NotNull(message = "Monto Operacio Requerido")
  private Double montoOperacion;

  @NotEmpty(message = "Observacion Requerida")
  private String observacionTransaccion;
  
}