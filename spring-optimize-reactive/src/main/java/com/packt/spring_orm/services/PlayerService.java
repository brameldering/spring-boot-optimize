package com.packt.spring_orm.services;

import com.packt.spring_orm.mappers.PlayerMapper;
import com.packt.spring_orm.records.*;
import com.packt.spring_orm.repositories.*;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {
  private final PlayerRepository playerRepository;

  public PlayerService(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  public Flux<Player> getPlayers() {
    return playerRepository.findAll().map(PlayerMapper::map);
  }

  //   @Cacheable(value = "players")
  public Mono<Player> getPlayer(Long id) {
    return playerRepository.findById(id)
        .map(PlayerMapper::map);
  }

  public Mono<Player> getPlayerByName(String name) {
    return playerRepository.findByName(name)
        .map(PlayerMapper::map);
  }

}
