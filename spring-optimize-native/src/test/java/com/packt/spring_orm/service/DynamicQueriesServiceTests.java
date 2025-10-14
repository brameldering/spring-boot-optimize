package com.packt.spring_orm.service;

import com.packt.spring_orm.records.*;
import com.packt.spring_orm.services.AlbumService;
import com.packt.spring_orm.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNot.not;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.packt.spring_orm.entities.MatchEventEntity;
import com.packt.spring_orm.entities.PlayerEntity;
import com.packt.spring_orm.services.DynamicQueriesService;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = DynamicQueriesServiceTests.Initializer.class)
public class DynamicQueriesServiceTests extends AbstractTestInit {

  @Autowired
  DynamicQueriesService dynamicQueriesService;

  @Autowired
  UserService userService;

  @Autowired
  AlbumService albumService;

  @Test
  public void searchTeamPlayersTest() {
    List<PlayerEntity> players = dynamicQueriesService.searchTeamPlayers(1884881L, Optional.of("ila"),
        Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    assertThat(players, not(empty()));

    players = dynamicQueriesService.searchTeamPlayers(1884881L, Optional.of("3$@"), Optional.empty(),
        Optional.empty(), Optional.empty(), Optional.empty());
    assertThat(players, empty());

    players = dynamicQueriesService.searchTeamPlayers(1884881L, Optional.empty(), Optional.of(170), Optional.of(190),
        Optional.empty(), Optional.empty());
    assertThat(players, not(empty()));

    players = dynamicQueriesService.searchTeamPlayers(1884881L, Optional.empty(), Optional.of(190), Optional.of(200),
        Optional.empty(), Optional.empty());
    assertThat(players, empty());

    players = dynamicQueriesService.searchTeamPlayers(1884881L, Optional.empty(), Optional.empty(), Optional.empty(),
        Optional.of(40), Optional.of(100));
    assertThat(players, not(empty()));

    players = dynamicQueriesService.searchTeamPlayers(1884881L, Optional.empty(), Optional.empty(), Optional.empty(),
        Optional.of(100), Optional.of(140));
    assertThat(players, empty());
  }

  @Test
  public void searchTeamPlayersAndMapTest() {
    List<Player> players = dynamicQueriesService.searchTeamPlayersAndMap(1884881L, Optional.of("ila"),
        Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    assertThat(players, not(empty()));

    players = dynamicQueriesService.searchTeamPlayersAndMap(1884881L, Optional.of("3$@"), Optional.empty(),
        Optional.empty(), Optional.empty(), Optional.empty());
    assertThat(players, empty());

    players = dynamicQueriesService.searchTeamPlayersAndMap(1884881L, Optional.empty(), Optional.of(170),
        Optional.of(190), Optional.empty(), Optional.empty());
    assertThat(players, not(empty()));

    players = dynamicQueriesService.searchTeamPlayersAndMap(1884881L, Optional.empty(), Optional.of(190),
        Optional.of(200), Optional.empty(), Optional.empty());
    assertThat(players, empty());

    players = dynamicQueriesService.searchTeamPlayersAndMap(1884881L, Optional.empty(), Optional.empty(),
        Optional.empty(), Optional.of(40), Optional.of(100));
    assertThat(players, not(empty()));

    players = dynamicQueriesService.searchTeamPlayersAndMap(1884881L, Optional.empty(), Optional.empty(),
        Optional.empty(), Optional.of(100), Optional.of(140));
    assertThat(players, empty());
  }

  @Test
  public void searchMatchEventsRangeTest() {
    List<MatchEventEntity> events = dynamicQueriesService.searchMatchEventsRange(400222854L, Optional.empty(),
        Optional.empty());
    assertThat(events, not(empty()));
    assertThat(events, hasSize(227));

    events = dynamicQueriesService.searchMatchEventsRange(400222854L,
        Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)), Optional.empty());
    assertThat(events, not(empty()));
    assertThat(events, hasSize(201));

    events = dynamicQueriesService.searchMatchEventsRange(400222854L,
        Optional.empty(), Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)));
    assertThat(events, not(empty()));
    assertThat(events, hasSize(26));

    events = dynamicQueriesService.searchMatchEventsRange(400222854L,
        Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)),
        Optional.of(LocalDateTime.of(2023, 7, 21, 5, 10, 0)));
    assertThat(events, not(empty()));
    assertThat(events, hasSize(2));

    events = dynamicQueriesService.searchMatchEventsRange(400222854L,
        Optional.of(LocalDateTime.of(2024, 8, 16, 10, 2, 0)),
        Optional.of(LocalDateTime.of(2024, 8, 16, 10, 4, 0)));
    assertThat(events, empty());
  }


  @Test
  void searchMatchEventsRangeAndMap(){
    List<MatchEvent> events = dynamicQueriesService.searchMatchEventsRangeAndMap(400222854L, Optional.empty(),
        Optional.empty());
    assertThat(events, not(empty()));
    assertThat(events, hasSize(227));

    events = dynamicQueriesService.searchMatchEventsRangeAndMap(400222854L,
        Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)), Optional.empty());
    assertThat(events, not(empty()));
    assertThat(events, hasSize(201));

    events = dynamicQueriesService.searchMatchEventsRangeAndMap(400222854L,
        Optional.empty(), Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)));
    assertThat(events, not(empty()));
    assertThat(events, hasSize(26));

    events = dynamicQueriesService.searchMatchEventsRangeAndMap(400222854L,
        Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)),
        Optional.of(LocalDateTime.of(2023, 7, 21, 5, 10, 0)));
    assertThat(events, not(empty()));
    assertThat(events, hasSize(2));

    events = dynamicQueriesService.searchMatchEventsRangeAndMap(400222854L,
        Optional.of(LocalDateTime.of(2024, 8, 16, 10, 2, 0)),
        Optional.of(LocalDateTime.of(2024, 8, 16, 10, 4, 0)));
    assertThat(events, empty());

  }

  @Test
  void deleteEventRangeTest() {
    List<MatchEventEntity> events = dynamicQueriesService.searchMatchEventsRange(400258556L, Optional.empty(),
        Optional.empty());
    assertThat(events, not(empty()));
    assertThat(events, hasSize(258));

    dynamicQueriesService.deleteEventRange(400258556L, LocalDateTime.of(2023, 8, 16, 10, 2, 0),
        LocalDateTime.of(2023, 8, 16, 10, 4, 0));

    events = dynamicQueriesService.searchMatchEventsRange(400258556L, Optional.empty(),
        Optional.empty());
    assertThat(events, not(empty()));
    assertThat(events, hasSize(252));

  }

  @Test
  void searchUserMissingPlayers() {
    User user1 = this.userService.createUser("user1");
    List<PlayerEntity> players = dynamicQueriesService.searchUserMissingPlayers(user1.id());
    assertThat(players, not(empty()));
    assertThat(players, hasSize(736));

    Album album = albumService.buyAlbum(user1.id(), "album1");
    List<Card> cards = albumService.buyCards(user1.id(), 1);
    albumService.useAllCardAvailable(user1.id());
    players = dynamicQueriesService.searchUserMissingPlayers(user1.id());
    assertThat(players, hasSize(735));
  }

  @Test
  void searchUserMissingPlayersAndMap() {
    User user1 = this.userService.createUser("user1");
    List<Player> players = dynamicQueriesService.searchUserMissingPlayersAndMap(user1.id());
    assertThat(players, not(empty()));
    assertThat(players, hasSize(736));

    Album album = albumService.buyAlbum(user1.id(), "album1");
    List<Card> cards = albumService.buyCards(user1.id(), 1);
    albumService.useAllCardAvailable(user1.id());
    players = dynamicQueriesService.searchUserMissingPlayersAndMap(user1.id());
    assertThat(players, hasSize(735));
  }

}

