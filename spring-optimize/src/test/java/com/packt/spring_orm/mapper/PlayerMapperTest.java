package com.packt.spring_orm.mapper;

import com.packt.spring_orm.entities.PlayerEntity;
import com.packt.spring_orm.mappers.PlayerMapper;
import com.packt.spring_orm.records.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;

public class PlayerMapperTest {
  @Test
  void map() {
    // Given
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setId(377762L);
    playerEntity.setName("Aitana BONMATI");
    playerEntity.setJerseyNumber(6);
    playerEntity.setPosition("Midfielder");
    playerEntity.setDateOfBirth(LocalDate.of(1998, 1, 18));

    // When
    PlayerMapper playerMapper = new PlayerMapper();
    Player player = playerMapper.map(playerEntity);

    // Then
    assertEquals(377762L, player.id());
    assertEquals("Aitana BONMATI", player.name());
    assertEquals(6, player.jerseyNumber());
    assertEquals("Midfielder", player.position());
    assertEquals(LocalDate.of(1998, 1, 18), player.dateOfBirth());
  }
}
