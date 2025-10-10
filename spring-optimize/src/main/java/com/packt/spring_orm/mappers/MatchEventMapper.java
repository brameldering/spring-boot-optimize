package com.packt.spring_orm.mappers;

import com.packt.spring_orm.entities.MatchEventEntity;
import com.packt.spring_orm.records.MatchEvent;
import org.springframework.stereotype.Component;

@Component
public class MatchEventMapper {
  public MatchEvent map(MatchEventEntity matchEventEntity) {
    return new MatchEvent(matchEventEntity.getTime(), matchEventEntity.getDetails());
  }
}