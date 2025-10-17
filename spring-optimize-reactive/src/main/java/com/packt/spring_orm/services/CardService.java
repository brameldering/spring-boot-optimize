package com.packt.spring_orm.services;

import com.packt.spring_orm.entities.AlbumEntity;
import com.packt.spring_orm.entities.CardEntity;
import com.packt.spring_orm.entities.PlayerEntity;
import com.packt.spring_orm.mappers.CardMapper;
import com.packt.spring_orm.records.*;
import com.packt.spring_orm.repositories.AlbumRepository;
import com.packt.spring_orm.repositories.CardRepository;
import com.packt.spring_orm.repositories.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CardService {

  private final static Logger log = LoggerFactory.getLogger(CardService.class);

  private final AlbumRepository albumRepository;
  private final CardRepository cardRepository;
  private final PlayerRepository playerRepository;

  public CardService(AlbumRepository albumRepository, CardRepository cardRepository, PlayerRepository playerRepository) {
    this.albumRepository = albumRepository;
    this.cardRepository = cardRepository;
    this.playerRepository = playerRepository;
  }

  public Mono<Card> getCard(Long cardId) {
    return cardRepository.findById(cardId)
        .flatMap(this::retrieveRelations)
        .switchIfEmpty(Mono.empty());
  }

  public Flux<Card> getCards() {
    return cardRepository.findAll()
        .flatMap(this::retrieveRelations)
        .onErrorResume(e -> {
          System.err.println("Error: " + e.getMessage());
          return Flux.empty();
        });
  }

  public Mono<Card> createCard(Long playerId) {
    CardEntity cardEntity = new CardEntity();
    cardEntity.setPlayerId(playerId);
    return cardRepository.save(cardEntity)
        .flatMap(c ->
            this.playerRepository.findById(playerId)
                .map(player -> CardMapper.map(c, Optional.empty(), player)));
  }

  protected Mono<Card> retrieveRelations(CardEntity cardEntity) {
    Mono<PlayerEntity> playerEntityMono = playerRepository.findById(cardEntity.getPlayerId());
    Mono<Optional<AlbumEntity>> albumEntityMono;
    if (cardEntity.getAlbumId() != null
        && cardEntity.getAlbumId().isPresent()) {
      albumEntityMono = albumRepository.findById(
              cardEntity.getAlbumId().get())
          .map(Optional::of);
    } else {
      albumEntityMono = Mono.just(Optional.empty());
    }
    return Mono.zip(playerEntityMono, albumEntityMono)
        .map(tuple ->
            CardMapper.map(cardEntity,
                tuple.getT2(), tuple.getT1()));
  }

}
