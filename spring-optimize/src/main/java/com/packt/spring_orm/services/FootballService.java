package com.packt.spring_orm.services;

import com.packt.spring_orm.entities.MatchEntity;
import com.packt.spring_orm.entities.MatchEventEntity;
import com.packt.spring_orm.entities.PlayerEntity;
import com.packt.spring_orm.entities.TeamEntity;
import com.packt.spring_orm.mappers.MatchEventMapper;
import com.packt.spring_orm.mappers.PlayerMapper;
import com.packt.spring_orm.records.*;
import com.packt.spring_orm.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FootballService {
  private final PlayerRepository playerRepository;
  private final TeamRepository teamRepository;
  private final MatchRepository matchRepository;
  private final AlbumRepository albumRepository;
  private final MatchEventRepository matchEventRepository;
  private final PlayerMapper playerMapper;
  private final MatchEventMapper matchEventMapper;

  public FootballService(PlayerRepository playerRepository, TeamRepository teamRepository,MatchRepository matchRepository, AlbumRepository albumRepository, MatchEventRepository matchEventRepository, PlayerMapper playerMapper,  MatchEventMapper matchEventMapper) {
    this.playerRepository = playerRepository;
    this.teamRepository = teamRepository;
    this.matchRepository = matchRepository;
    this.albumRepository = albumRepository;
    this.matchEventRepository = matchEventRepository;
    this.playerMapper = playerMapper;
    this.matchEventMapper = matchEventMapper;
  }

  // ================ Team ================

  //  public List<Team> getTeams() {
  //    return StreamSupport.stream(teamRepository.findAll().spliterator(), false)
  //        .map(t -> new Team(t.getId(), t.getName(), List.of())).toList();
  //  }

  public List<Team> getAllTeams() {
    return teamRepository.findAllWithPlayers().stream()
        .map(teamEntity -> new Team(
            teamEntity.getId(),
            teamEntity.getName(),
            teamEntity.getPlayers().stream()
                .map(playerMapper::map)
                .toList()))
        .toList();
  }

  // Transactional so that context remains so players for the team can be loaded lazy
  @Transactional(readOnly = true)
  public Team getTeam(Long id) {
    TeamEntity teamEntity = teamRepository.findByIdWithPlayers(id).orElse(null);
    if (teamEntity == null) {
      return null;
    } else {
      return new Team(teamEntity.getId(),
          teamEntity.getName(),
          teamEntity.getPlayers()
              .stream()
              .map(playerMapper::map)
              .toList());
    }
  }

  public Team createTeam(String name) {
    TeamEntity teamEntity = new TeamEntity();
    teamEntity.setName(name);
    teamEntity = teamRepository.save(teamEntity);
    return new Team(teamEntity.getId(), teamEntity.getName(), List.of());
  }

  public List<TeamPlayers> getNumberOfPlayersByPosition(String position) {
    return teamRepository.getNumberOfPlayersByPosition(position);
  }

  // ================ Player ================


  public Player getPlayer(Long id) {
    return playerRepository.findById(id)
        .map(playerMapper::map).orElse(null);
  }

  public Team getPlayerTeam(Long id) {
    return playerRepository.findByIdWithTeam(id).map(p -> new Team(p.getTeam().getId(), p.getTeam().getName(), List.of())).orElse(null);
  }

  public List<Player> searchPlayers(String name) {
    return playerRepository.findByNameContainingIgnoreCase(name)
        .stream()
        .map(playerMapper::map)
        .toList();
  }

  public List<Player> searchPlayersLike(String q) {
    return playerRepository.findByNameLike("%" + q + "%")
        .stream()
        .map(playerMapper::map)
        .toList();
  }

  public List<Player> searchPlayersStartingWith(String nameStarting) {
    return playerRepository.findByNameStartingWith(nameStarting)
        .stream()
        .map(playerMapper::map)
        .toList();
  }

  public List<Player> searchPlayersByDateOfBirth(LocalDate dateOfBirth) {
    return playerRepository.findByDateOfBirth(dateOfBirth)
        .stream()
        .map(playerMapper::map)
        .toList();
  }

  public List<Player> getPlayersList(List<Long> players) {
    // return playerRepository.findListOfPlayers(players)
    return playerRepository.findByIdIn(players)
        .stream()
        .map(playerMapper::map)
        .toList();
  }

  public List<Player> getTeamPlayers(Long id) {
    return playerRepository.findByTeamId(id, Sort.by("name").ascending()).stream()
        .map(playerMapper::map)
        .toList();
  }

  public List<Player> getAllPlayersPaged(int pageNumber, int size) {
    Page<PlayerEntity> page = playerRepository.findAll(Pageable.ofSize(size).withPage(pageNumber));
    return page.stream()
        .map(playerMapper::map)
        .toList();
  }

  public Player updatePlayerPosition(Long id, String position) {
    PlayerEntity playerEntity = playerRepository.findById(id).orElse(null);
    if (playerEntity == null) {
      return null;
    }  else {
      playerEntity.setPosition(position);
      playerEntity = playerRepository.save(playerEntity);
      return playerMapper.map(playerEntity);
    }
  }

  public Integer getTotalPlayersWithMoreThanNMatches(int num_matches) {
    return playerRepository.getTotalPlayersWithMoreThanNMatches(num_matches);
  }

  // ================ Match ================
  // Following correspond to MatchEvent data which are retrieved
  // by native queries in the MatchEvent Repository

  public Match getMatchWithTimeline(Long matchId) {
    MatchEntity match = matchRepository.findByIdWithTimeline(matchId).orElse(null);
    if (match != null) {
      return new Match(match.getId(), match.getTeam1().getName(), match.getTeam2().getName(),
          match.getTeam1Goals(), match.getTeam2Goals(), match.getMatchDate(),
          match.getEvents()
              .stream()
              .map(matchEventMapper::map)
              .toList());
    } else {
      return null;
    }
  }

  public List<MatchEvent> getMatchWithPlayerEvents(Long matchId, Long playerId) {
    List<MatchEventEntity> matchEvents = matchEventRepository.findByMatchIdAndPlayer(matchId, playerId);

    return matchEvents.stream()
        .map(matchEventMapper::map)
        .toList();
  }

  public List<MatchEvent> getMatchEventsOfType(Long matchId, Integer eventType) {
    return matchEventRepository.findByIdIncludeEventsOfType(matchId, eventType).stream()
        .map(matchEventMapper::map)
        .toList();
  }

  public List<Player> getPlayersByMatch(Long id) {
    return matchRepository.findPlayersByMatchId(id)
        .stream()
        .map(playerMapper::map)
        .toList();
  }

  public List<MatchEvent> getMatchWithPlayerEventsError(Long matchId, Long playerId) {
    List<MatchEventEntity> matchEvents = matchEventRepository.findByMatchIdAndPlayerError(matchId, playerId);

    return matchEvents.stream()
        .map(e -> new MatchEvent(e.getTime(), e.getDetails()))
        .toList();
  }

  // ================ Album ================

  public List<Player> getAlbumPlayers(Long id) {
    return albumRepository.findByIdPlayers(id, Pageable.ofSize(10).withPage(0))
        .stream()
        .map(playerMapper::map)
        .toList();
  }

  public List<Player> getAlbumMissingPlayers(Long id) {
    return albumRepository.findByIdMissingPlayers(id)
        .stream()
        .map(playerMapper::map)
        .toList();
  }

  public List<Player> getAlbumPlayersByTeam(Long albumId, Long teamId) {
    return albumRepository.findByIdAndTeam(albumId, teamId)
        .stream()
        .map(playerMapper::map)
        .toList();
  }

}
