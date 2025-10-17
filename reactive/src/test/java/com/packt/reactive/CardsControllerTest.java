package com.packt.reactive;

import com.packt.reactive.controllers.CardsController;
import com.packt.reactive.records.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(CardsController.class)
public class CardsControllerTest {
  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void testGetCards() {
    webTestClient.get().uri("/cards").exchange().expectStatus().isOk().expectBodyList(Card.class);
  }

  @Test
  public void testGetException() {
    webTestClient.get().uri("/cards/exception").exchange().expectStatus().isBadRequest().expectBody(ProblemDetail.class);
  }

}
