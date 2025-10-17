package com.packt.spring_orm.repositories;

import com.packt.spring_orm.entities.PlayerEntity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PlayerRepository extends ReactiveCrudRepository<PlayerEntity, Long> {
  public Mono<PlayerEntity> findByName(String name);
}
