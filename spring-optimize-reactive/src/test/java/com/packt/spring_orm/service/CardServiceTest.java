package com.packt.spring_orm.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.packt.spring_orm.records.Card;
import com.packt.spring_orm.services.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = CardServiceTest.Initializer.class)
public class CardServiceTest extends AbstractTestInit {

  @Autowired
  CardService cardService;

  @Test
  void createCard() {
    // ARRANGE
    Long playerId = 396930L;
    Card card = cardService.createCard(playerId).block();

    assertNotNull(card);
    assertEquals(playerId, card.player().id());
    assertTrue(card.album().isEmpty());
    assertEquals("Ona BATLLE", card.player().name());
    assertEquals("Defender", card.player().position());
  }

  @Test
  void getCard() {
    // ARRANGE
    Long playerId = 396930L;
    Card card = cardService.createCard(playerId).block();

    // ACT
    Card retrievedCard = cardService.getCard(card.id()).block();
    assertNotNull(retrievedCard);
    assertEquals(playerId, retrievedCard.player().id());
    assertTrue(retrievedCard.album().isEmpty());
    assertEquals(card.id(), retrievedCard.id());
    assertEquals("Ona BATLLE", retrievedCard.player().name());
    assertEquals("Defender", retrievedCard.player().position());
  }

  @Test
  void getCards() {
    // ARRANGE
    Long playerId = 396930L;
    Card card = cardService.createCard(playerId).block();

    // ACT
    List<Card> cards = cardService.getCards().collectList().block();
    assertTrue(cards.size() > 0);
    Card retrievedCard = cards.stream().filter(c -> c.id().equals(card.id())).findFirst().get();
    assertNotNull(retrievedCard);
    assertEquals(playerId, retrievedCard.player().id());
    assertTrue(retrievedCard.album().isEmpty());

    assertEquals(card.id(), retrievedCard.id());
    assertEquals("Ona BATLLE", retrievedCard.player().name());
    assertEquals("Defender", retrievedCard.player().position());
  }
}
