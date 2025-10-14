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
@RequestMapping("/teams")
public class TeamsController {

  private final FootballService footballService;

  public TeamsController(FootballService footballService) {
    this.footballService = footballService;
  }

  @GetMapping
  public List<Team> getTeams() {
    return footballService.getAllTeams();
  }

  @GetMapping("/{id}")
  public Team getTeam(@PathVariable Long id) {
    return footballService.getTeam(id);
  }

  @GetMapping("/{id}/players")
  public List<Player> getTeamPlayers(@PathVariable Long id) {
    return footballService.getTeamPlayers(id);
  }

  @PostMapping
  public Team createTeam(@RequestBody String name) {
    return footballService.createTeam(name);
  }

  @GetMapping("/{position}/count")
  public List<TeamPlayers> getNumberOfPlayersByPosition(@PathVariable String position) {
    return footballService.getNumberOfPlayersByPosition(position);
  }

}
