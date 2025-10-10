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
@RequestMapping("/players")
public class PlayersController {

  private final FootballService footballService;

  public PlayersController(FootballService footballService) {
    this.footballService = footballService;
  }

  @GetMapping
  public List<Player> searchPlayers(@RequestParam String name) {
    return footballService.searchPlayers(name);
  }

  @GetMapping("/{id}")
  public Player getPlayer(@PathVariable Long id) {
    return footballService.getPlayer(id);
  }

  @GetMapping("/birth/{date}")
  public List<Player> searchPlayersByBirthDate(@PathVariable LocalDate date) {
    return footballService.searchPlayersByDateOfBirth(date);
  }

  @PutMapping("/{id}/position")
  public Player updatePlayerPosition(@PathVariable Long id, @RequestBody String position) {
    return footballService.updatePlayerPosition(id, position);
  }

  @GetMapping("/list")
  public List<Player> getPlayersList(@RequestParam List<Long> players) {
    return footballService.getPlayersList(players);
  }

  @GetMapping("/startwith")
  public List<Player> searchPlayersStartingWith(@RequestParam String startingName) {
    return footballService.searchPlayersStartingWith(startingName);
  }

  @GetMapping("/search")
  public List<Player> searchPlayersLike(@RequestParam String q) {
    return footballService.searchPlayersLike(q);
  }

  @GetMapping("/paginated")
  public List<Player> getPlayers(@RequestParam Map<String, String> params) {
    Integer page = Integer.parseInt(params.getOrDefault("page", "0"));
    Integer size = Integer.parseInt(params.getOrDefault("size", "10"));
    return footballService.getAllPlayersPaged(page, size);
  }

}
