package com.packt.spring_orm.mappers;

import com.packt.spring_orm.entities.PlayerEntity;
import com.packt.spring_orm.records.Player;

public class PlayerMapper {
  public static Player map(PlayerEntity playerEntity) {
    return new Player(playerEntity.getId(), playerEntity.getName(), playerEntity.getJerseyNumber(),
        playerEntity.getPosition(), playerEntity.getDateOfBirth());
  }
}
