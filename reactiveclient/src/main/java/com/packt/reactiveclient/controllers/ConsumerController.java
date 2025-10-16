package com.packt.reactiveclient.controllers;

import com.packt.reactiveclient.records.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public ConsumerController(@Value("${footballservice.url}") String url) {
    logger.info("ConsumerController initialized");
    this.webClient = WebClient.create(url);
  }

  @GetMapping("/cards")
  public Flux<Card> getCards() {
    return webClient.get().uri("/cards").retrieve().bodyToFlux(Card.class).doOnSubscribe(sub -> logger.info("Received call to getCards"));
  }

  @GetMapping("/cards/{cardId}")
  public Mono<Card> getCard(@PathVariable("cardId") String cardId) {
    logger.info("getCard {}", cardId);
    return webClient.get().uri("/cards/" + cardId).retrieve().onStatus(code -> code.is4xxClientError(), response -> Mono.empty()).bodyToMono(Card.class).doOnSubscribe(sub -> logger.info("Received call to getCard")).doOnNext(card -> logger.info("Received card: {}", card)).doOnError(error -> logger.error("Error fetching card {}", cardId, error));
  }

  @GetMapping("/error")
  public Mono<String> getFailedRequest() {
    logger.info("getFailedRequest");
    return webClient.get().uri("/invalidpath").exchangeToMono(response ->{
      if (response.statusCode().equals(HttpStatus.NOT_FOUND)) return Mono.just("Remote server returned 404");
      else if (response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) return Mono.just("Remote server returned 500: " + response.bodyToMono(String.class));
      else return response.bodyToMono(String.class);
    });
  }
}
