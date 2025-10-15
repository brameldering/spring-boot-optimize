package com.packt.reactive.controllers;

import com.packt.reactive.exceptions.SampleException;
import com.packt.reactive.records.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardsController {

  @GetMapping
  public Flux<Card> getCards() {
    return Flux.fromIterable(List.of(new Card("1", "WWC23", "Ivana Andres", 7), new Card("2", "WWC23", "Alexia Putellas", 1)));
  }

  @GetMapping("/{cardId}")
  public Mono<Card> getCard(@PathVariable String cardId) {
    return Mono.just(new Card(cardId, "WWC23", "Superplayer", 1));
  }

  @GetMapping("/exception")
  public Mono<Card> getCardException() {
    throw new SampleException("This is a sample exception");
  }

  @ExceptionHandler(SampleException.class)
  public ProblemDetail handleSampleException(SampleException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    problemDetail.setDetail("sample exception");
    return problemDetail;
  }

}
