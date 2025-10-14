package com.packt.spring_orm.mappers;

import com.packt.spring_orm.entities.PlayerEntity;
import com.packt.spring_orm.records.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
  public Player map(PlayerEntity playerEntity) {
    return new Player(playerEntity.getId(), playerEntity.getName(), playerEntity.getJerseyNumber(),
        playerEntity.getPosition(), playerEntity.getDateOfBirth());
  }
}
