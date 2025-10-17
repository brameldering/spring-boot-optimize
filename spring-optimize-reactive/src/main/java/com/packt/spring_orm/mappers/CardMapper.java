package com.packt.spring_orm.mappers;

import com.packt.spring_orm.records.Card;
import com.packt.spring_orm.entities.AlbumEntity;
import com.packt.spring_orm.entities.CardEntity;
import com.packt.spring_orm.entities.PlayerEntity;

import java.util.Optional;

public class CardMapper {
    public static Card map(CardEntity cardEntity, Optional<AlbumEntity> albumEntity, PlayerEntity playerEntity) {
        return new Card(cardEntity.getId(), albumEntity.map(AlbumMapper::map), PlayerMapper.map(playerEntity));
    }
}
