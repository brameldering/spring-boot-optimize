package com.packt.spring_orm.service;

import com.packt.spring_orm.records.*;
import com.packt.spring_orm.repositories.TeamPlayers;
import com.packt.spring_orm.services.AlbumService;
import com.packt.spring_orm.services.FootballService;
import com.packt.spring_orm.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = FootballServiceTest.Initializer.class)
@ExtendWith(SpringExtension.class)
public class FootballServiceTest extends AbstractIntegrationInit{

  @Autowired
  private FootballService footballService;
  @Autowired
  UserService userService;
  @Autowired
  AlbumService albumService;

  @Test
  public void testCreateTeam() {
    Team team = footballService.createTeam("Jamaica");

    assertThat(team, notNullValue());
    assertThat(team.id(), notNullValue());
  }

  @Test
  public void testGetTeam() {
    Team team = footballService.getTeam(1884823L);
    assertThat(team, notNullValue());
    assertThat(team.players(), not(empty()));
    assertThat(team.players(), hasSize(23));
  }

  @Test
  void getTeams() {
    List<Team> teams = footballService.getAllTeams();
    assertThat(teams, not(empty()));
  }

  @Test
  public void testGetTeam_notFound() {
    assertThat(footballService.getTeam(999999L), nullValue());
  }

  @Test
  public void getPlayers() {
    List<Player> players = footballService.searchPlayers("Adriana");
    assertThat(players, not(empty()));
  }

  @Test
  void getPlayer() {
    Player player = footballService.getPlayer(396914L);
    assertThat(player, notNullValue());
    assertThat(player.name(), is("Laia CODINA"));
  }

  @Test
  void searchPlayers() {
    List<Player> players = footballService.searchPlayers("Alexia");
    assertThat(players, not(empty()));
    assertThat(players, hasSize(1));
  }

  @Test
  void searchPlayersStartingWith() {
    List<Player> players = footballService.searchPlayersStartingWith("Eu");
    assertThat(players, not(empty()));
    assertThat(players, hasSize(2));
  }

  @Test
  public void searchPlayersLike() {
    List<Player> players = footballService.searchPlayersLike("Adriana");
    assertThat(players, not(empty()));
  }

  @Test
  public void getPlayersByMatch() {
    List<Player> players = footballService.getPlayersByMatch(400222852L);
    assertThat(players, not(empty()));
  }

  @Test
  public void getPlayersByMatch_notFound() {
    List<Player> players = footballService.getPlayersByMatch(9999999L);
    assertThat(players, empty());
  }

  @Test
  public void getPlayersByBirthDate() {
    List<Player> players = footballService.searchPlayersByDateOfBirth(LocalDate.of(1994, 2, 4));
    assertThat(players, not(empty()));
  }

  @Test
  public void getAlbumMissingPlayers() {
    List<Player> players = footballService.getAlbumMissingPlayers(1L);
    assertThat(players, not(empty()));
  }

  @Test
  public void getAlbumPlayers() {
    User user1 = this.userService.createUser("user1");
    Album album = albumService.buyAlbum(user1.id(), "album1");
    List<Player> players = footballService.getAlbumPlayers(album.id());
    assertThat(players, empty());

    List<Card> cards = albumService.buyCards(user1.id(), 1);
    albumService.useAllCardAvailable(user1.id());
    players = footballService.getAlbumPlayers(album.id());
    assertThat(players, not(empty()));
    assertThat(players, hasSize(1));
  }

  @Test
  public void getAlbumPlayersByTeam() {
    User user1 = this.userService.createUser("user1");
    Album album = albumService.buyAlbum(user1.id(), "album1");

    List<Card> cards = albumService.buyCards(user1.id(), 1);
    cards = albumService.useAllCardAvailable(user1.id());
    Team team = footballService.getPlayerTeam(cards.getFirst().player().id());
    List<Player> players = footballService.getAlbumPlayersByTeam(album.id(), team.id());
    assertThat(players, not(empty()));
    assertThat(players, hasSize(1));
  }

  @Test
  public void getPlayersList() {
    List<Player> players = footballService.getPlayersList(List.of(357669L, 420326L, 420324L));
    assertThat(players, not(empty()));
    assertThat(players, hasSize(3));
  }

  @Test
  void createTeam() {
    Team team1 = footballService.createTeam("Senegal");
    assertThat(team1, notNullValue());
    assertThat(team1.id(), notNullValue());
  }

  @Test
  void updatePlayerPosition() {
    Player beforePlayer = footballService.getPlayer(396930L);
    assertThat(beforePlayer, notNullValue());
    assertThat(beforePlayer.position(), notNullValue());
    assertThat(beforePlayer.position(), not("Midfielder"));
    Player afterPlayer = footballService.updatePlayerPosition(396930L, "Midfielder");
    assertThat(afterPlayer, notNullValue());
    assertThat(afterPlayer.position(), notNullValue());
    assertThat(afterPlayer.position(), is("Midfielder"));
  }

  @Test
  public void getTeamPlayers() {
    List<Player> players = footballService.getTeamPlayers(1884823L);
    assertThat(players, not(empty()));
  }

  @Test
  public void getTeamPlayers_notFound() {
    List<Player> players = footballService.getTeamPlayers(9999999L);
    assertThat(players, empty());
  }

  @Test
  public void getAllPlayersPaged() {
    List<Player> players = footballService.getAllPlayersPaged(0, 10);
    assertThat(players, not(empty()));
    assertThat(players, hasSize(10));
  }

  @Test
  public void getNumberOfPlayersByPosition() {
    List<TeamPlayers> players = footballService.getNumberOfPlayersByPosition("Midfielder");
    assertThat(players, not(empty()));
  }

  @Test
  public void getMatchEventsOfType() {
    List<MatchEvent> events = footballService.getMatchEventsOfType(400222852L, 24);
    assertThat(events, not(empty()));
  }

  @Test
  public void getMatchEventsOfType_notFound() {
    List<MatchEvent> events = footballService.getMatchEventsOfType(9999999L, 24);
    assertThat(events, empty());
  }

  @Test
  public void getMatchWithPlayerEvents() {
    List<MatchEvent> events = footballService.getMatchWithPlayerEvents(400222852L, 358189L);
    assertThat(events, not(empty()));
  }

  @Test
  public void getMatchWithPlayerEvents_notFound() {
    List<MatchEvent> events = footballService.getMatchWithPlayerEvents(9999999L, 358189L);
    assertThat(events, empty());
  }

  @Test
  public void getMatchWithPlayerEventsError() {
    assertThrows(InvalidDataAccessResourceUsageException.class,
        () -> footballService.getMatchWithPlayerEventsError(400222852L, 358189L));
  }

}
