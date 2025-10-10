package com.packt.spring_orm.controllers;

import com.packt.spring_orm.records.Match;
import com.packt.spring_orm.records.MatchEvent;
import com.packt.spring_orm.records.Player;
import com.packt.spring_orm.records.Team;
import com.packt.spring_orm.repositories.TeamPlayers;
import com.packt.spring_orm.services.FootballService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/football")
public class FootballController {

  private final FootballService footballService;

  public FootballController(FootballService footballService) {
    this.footballService = footballService;
  }

  @GetMapping("/matches/{id}/players")
  public List<Player> getPlayersByMatch(@PathVariable Long id) {
    return footballService.getPlayersByMatch(id);
  }

  @GetMapping("/albums/{id}/{teamId}/players")
  public List<Player> getAlbumTeamPlayers(@PathVariable Long id, @PathVariable Long teamId) {
    return footballService.getAlbumPlayersByTeam(id, teamId);
  }

  @GetMapping("/albums/{id}/missingplayers")
  public List<Player> getAlbumMissingPlayers(@PathVariable Long id) {
    return footballService.getAlbumMissingPlayers(id);
  }

  @GetMapping("/albums/{id}/myplayers")
  public List<Player> getAlbumMyPlayers(@PathVariable Long id) {
    return footballService.getAlbumPlayers(id);
  }

  // MatchEvents
  @GetMapping("/matches/{id}/timeline")
  public Match getMatchWithTimeline(@PathVariable Long id) {
    return footballService.getMatchWithTimeline(id);
  }

  @GetMapping("/matches/{id}/timeline/{playerId}")
  public List<MatchEvent> getMatchWithTimeline(@PathVariable Long id, @PathVariable Long playerId) {
    return footballService.getMatchWithPlayerEvents(id, playerId);
  }
  @GetMapping("/matches/{id}/timeline/events/{type}")
  public List<MatchEvent> getMatchWithEventsOfType(@PathVariable Long id, @PathVariable Integer type) {
    return footballService.getMatchEventsOfType(id, type);
  }

  @GetMapping("/players/matches/{numMatches}")
  public Integer getTotalPlayersWithMoreThanNMatches(@PathVariable Integer numMatches) {
    return footballService.getTotalPlayersWithMoreThanNMatches(numMatches);
  }

  @GetMapping("/matches/{id}/timeline/{playerId}/error")
  public List<MatchEvent> getMatchWithTimelineError(@PathVariable Long id, @PathVariable Long playerId) {
    return footballService.getMatchWithPlayerEventsError(id, playerId);
  }

}
