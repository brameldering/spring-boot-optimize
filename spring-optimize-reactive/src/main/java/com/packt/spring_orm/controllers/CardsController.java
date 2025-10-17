package com.packt.spring_orm.controllers;

import com.packt.spring_orm.records.Card;
import com.packt.spring_orm.services.CardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cards")
public class CardsController {

  private final CardService cardService;

  public CardsController(CardService cardService) {
    this.cardService = cardService;
  }

  @GetMapping
  public Flux<Card> getCards() {
    return cardService.getCards();
  }

  @GetMapping("/{cardId}")
  public Mono<Card> getCard(@PathVariable Long cardId) {
    return cardService.getCard(cardId);
  }

}
