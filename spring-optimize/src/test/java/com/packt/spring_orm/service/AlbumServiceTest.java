package com.packt.spring_orm.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.packt.spring_orm.records.Album;
import com.packt.spring_orm.records.Card;
import com.packt.spring_orm.records.TradingUser;
import com.packt.spring_orm.records.User;
import com.packt.spring_orm.services.AlbumService;
import com.packt.spring_orm.services.UserService;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = AlbumServiceTest.Initializer.class)
public class AlbumServiceTest extends AbstractIntegrationInit {

  @Autowired
  AlbumService albumService;
  @Autowired
  UserService userService;

  @Test
  @Description("Test buy album")
  public void testBuyAlbum() {

    // ARRANGE
    User user = userService.createUser("Test");
    Album album = albumService.buyAlbum(user.id(), "Sample album");

    assertEquals("Sample album", album.title());
    assertEquals(user.id(), album.ownerId());

  }

  @Test
  public void testBuyCards() {
    User user = userService.createUser("Test");
    List<Card> cards = albumService.buyCards(user.id(), 5);
    assertThat(cards, hasSize(5));
  }

  @Test
  public void addCardToAlbum() {
    // ARRANGE
    User user = userService.createUser("Test");
    Album album1 = albumService.buyAlbum(user.id(), "sample album");
    List<Card> cards = albumService.buyCards(user.id(), 5);

    // ACT
    Card card = albumService.addCardToAlbum(cards.getFirst().id(), album1.id());
    assertThat(card.albumId(), notNullValue());
  }

  @Test
  public void useAllCardAvailable() {
    // ARRANGE
    User user = userService.createUser("Test");
    albumService.buyAlbum(user.id(), "sample album");
    albumService.buyCards(user.id(), 5);

    // ACT
    List<Card> usedCards = albumService.useAllCardAvailable(user.id());
    assertThat(usedCards, hasSize(5));
    for (Card card : usedCards) {
      assertThat(card.albumId(), notNullValue());
    }
  }

  @Test
  void transferCard() {
    User user1 = userService.createUser("Test");
    User user2 = userService.createUser("Test2");
    Album album1 = albumService.buyAlbum(user1.id(), "sample album");
    List<Card> cards = albumService.buyCards(user1.id(), 5);
    Card card = albumService.addCardToAlbum(cards.getFirst().id(), album1.id());

    Optional<Card> transferredCard = albumService.transferCard(card.id(), user2.id());
    assertThat(transferredCard, notNullValue());
    assertTrue(transferredCard.flatMap(Card::albumId).isEmpty());
    assertEquals(user2.id(), transferredCard.get().ownerId());
  }

  @Test
  void transferCard_cardNotExists() {
    userService.createUser("Test");
    User user2 = userService.createUser("Test2");

    Optional<Card> transferredCard = albumService.transferCard(new Random().nextLong(), user2.id());
    assertTrue(transferredCard.isEmpty());
  }

  @Test
  void tradeAllCards() {
    // ARRANGE
    User user1 = userService.createUser("Test");
    User user2 = userService.createUser("Test2");
    albumService.buyAlbum(user1.id(), "sample album");
    albumService.buyAlbum(user2.id(), "sample album");
    albumService.buyCards(user1.id(), 800);
    albumService.buyCards(user2.id(), 800);
    albumService.useAllCardAvailable(user1.id());
    albumService.useAllCardAvailable(user2.id());

    List<Card> tradedCards = albumService.tradeAllCards(user1.id(), user2.id());
    assertThat(tradedCards, hasItems());
  }

  @Test
  void getUserWithCardsAndAlbums() {
    User user1 = userService.createUser("Test");
    Album album1 = albumService.buyAlbum(user1.id(), "sample album");
    List<Card> cards = albumService.buyCards(user1.id(), 5);
    albumService.addCardToAlbum(cards.getFirst().id(), album1.id());

    Optional<TradingUser> userCards = albumService.getUserWithCardsAndAlbums(user1.id());
    assertThat(userCards, notNullValue());
    assertThat(userCards.get().cards(), hasSize(5));
    assertThat(userCards.get().albums(), hasSize(1));
  }
}
