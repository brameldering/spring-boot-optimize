package com.packt.spring_orm.mappers;

import com.packt.spring_orm.records.Album;
import com.packt.spring_orm.entities.AlbumEntity;

public class AlbumMapper {
    public static Album map(AlbumEntity albumEntity) {
      return new Album(albumEntity.getId(), albumEntity.getTitle());
    }
}
