package com.packt.spring_orm.repositories;

import com.packt.spring_orm.entities.CardEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CardRepository extends ReactiveCrudRepository<CardEntity, Long> {
}
