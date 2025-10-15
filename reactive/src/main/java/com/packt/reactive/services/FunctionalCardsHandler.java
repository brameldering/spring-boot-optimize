package com.packt.reactive.services;

import java.util.List;

import com.packt.reactive.records.Card;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FunctionalCardsHandler {
  public Flux<Card> getCards() {
    return Flux.fromIterable(
        List.of(new Card("1", "WWC23", "Ivana Andres", 7), new Card("2", "WWC23", "Alexia Putellas", 1)));
  }

  public Mono<Card> getCard(String cardId) {
    return Mono.just(new Card(cardId, "WWC23", "Superplayer", 1));
  }
}