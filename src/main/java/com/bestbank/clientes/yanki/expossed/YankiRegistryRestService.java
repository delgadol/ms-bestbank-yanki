package com.bestbank.clientes.yanki.expossed;

import com.bestbank.clientes.yanki.bussiness.dto.req.ClienteYankiReq;
import com.bestbank.clientes.yanki.bussiness.dto.req.TransacionYankiReq;
import com.bestbank.clientes.yanki.bussiness.dto.res.AppYankiNotificacion;
import com.bestbank.clientes.yanki.bussiness.services.YankiOperationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/yanki")
public class YankiRegistryRestService {
  
  @Autowired
  private YankiOperationService appService;
  
  
  
  @GetMapping({"", "/"})
  public Mono<AppYankiNotificacion> getTestValue(
      @RequestParam(name = "txt", defaultValue = "") String message) {
    return null;
    
  }
  
  @PostMapping("/clientes")
  public Mono<AppYankiNotificacion> postYankiClientRegister(
      @Valid @RequestBody ClienteYankiReq cliente) {
    return appService.postCliente(cliente);
  }
  
  @PostMapping("/operaciones")
  public Mono<AppYankiNotificacion> postYankiOperaciones(
      @Valid @RequestBody TransacionYankiReq transacion) {
    return appService.postOperacionData(transacion);
  }
  
  

}
