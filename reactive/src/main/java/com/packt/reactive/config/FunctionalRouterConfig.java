package com.packt.reactive.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.packt.reactive.records.Card;
import com.packt.reactive.services.FunctionalCardsHandler;

@Configuration
public class FunctionalRouterConfig {
  @Bean
  FunctionalCardsHandler cardsHandler() {
    return new FunctionalCardsHandler();
  }

  @Bean
  RouterFunction<ServerResponse> getCards() {
    return route(GET("/funcards"), req -> ok().body(cardsHandler().getCards(), Card.class));
  }

  @Bean
  RouterFunction<ServerResponse> getCard(){
    return route(GET("/funcards/{cardId}"), req -> ok().body(cardsHandler().getCard(req.pathVariable("cardId")), Card.class));
  }
}
