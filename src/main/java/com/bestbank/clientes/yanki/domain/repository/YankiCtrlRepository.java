package com.bestbank.clientes.yanki.domain.repository;

import com.bestbank.clientes.yanki.domain.model.AppYankiCtrl;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface YankiCtrlRepository extends ReactiveMongoRepository<AppYankiCtrl, String>{
  
  Mono<AppYankiCtrl> findFirstByNumeroTelefono(String numeroTelefono);
  
  
}
