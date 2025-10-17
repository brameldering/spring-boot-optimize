package com.packt.spring_orm.repositories;

import com.packt.spring_orm.entities.AlbumEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AlbumRepository extends ReactiveCrudRepository<AlbumEntity, Long> {
}
