package com.packt.reactiveclient.controllers;

import com.packt.reactiveclient.records.Card;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
  private final WebClient webClient;

  public ConsumerController(@Value("${footballservice.url}") String url) {
    this.webClient = WebClient.create(url);
  }

  @GetMapping("/cards")
  public Flux<Card> getCards() {
    return webClient.get().uri("/cards").retrieve().bodyToFlux(Card.class);
  }

  @GetMapping("/cards/{cardId}")
  public Mono<Card> getCard(@PathVariable("cardId") String cardId) {
    return webClient.get().uri("/cards/" + cardId).retrieve().onStatus(code -> code.is4xxClientError(), response -> Mono.empty()).bodyToMono(Card.class);
  }

  @GetMapping("/error")
  public Mono<String> getFailedRequest() {
    return webClient.get().uri("/invalidpath").exchangeToMono(response ->{
      if (response.statusCode().equals(HttpStatus.NOT_FOUND)) return Mono.just("Remote server returned 404");
      else if (response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) return Mono.just("Remote server returned 500: " + response.bodyToMono(String.class));
      else return response.bodyToMono(String.class);
    });
  }
}
