package com.packt.spring_orm.mappers;

import com.packt.spring_orm.records.Team;
import com.packt.spring_orm.entities.PlayerEntity;
import com.packt.spring_orm.entities.TeamEntity;

import java.util.stream.Stream;

public class TeamMapper {
    public static Team map(TeamEntity teamEntity, Stream<PlayerEntity> players) {
        return new Team(teamEntity.getId(),
                teamEntity.getName(),
                players.map(PlayerMapper::map)
                        .toList());
    }
}
