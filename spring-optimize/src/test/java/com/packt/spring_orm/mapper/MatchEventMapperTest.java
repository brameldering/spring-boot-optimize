package com.packt.spring_orm.mapper;

import com.packt.spring_orm.entities.MatchEventDetail;
import com.packt.spring_orm.entities.MatchEventEntity;
import com.packt.spring_orm.mappers.MatchEventMapper;
import com.packt.spring_orm.records.MatchEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchEventMapperTest {
  @Test
  void map() {
    // Given
    MatchEventDetail matchEventDetail = new MatchEventDetail();
    matchEventDetail.setType(1);
    matchEventDetail.setDescription("description");
    matchEventDetail.setPlayers(List.of(1,2,3));
    matchEventDetail.setMediaFiles(List.of("file1", "file2"));

    MatchEventEntity matchEventEntity = new MatchEventEntity();
    matchEventEntity.setId(377762L);
    matchEventEntity.setTime(LocalDateTime.of(1998, 1, 18, 14, 0, 0));
    matchEventEntity.setDetails(matchEventDetail);

    // When
    MatchEventMapper matchEventMapper = new MatchEventMapper();
    MatchEvent matchEvent = matchEventMapper.map(matchEventEntity);

    // Then
    assertEquals(LocalDateTime.of(1998, 1, 18, 14, 0, 0), matchEvent.time());
    assertEquals(matchEventDetail, matchEvent.details());
  }
}
