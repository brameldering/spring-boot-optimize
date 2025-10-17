package com.packt.spring_orm.service;

import com.packt.spring_orm.records.*;
import com.packt.spring_orm.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = PlayerServiceTest.Initializer.class)
@ExtendWith(SpringExtension.class)
public class PlayerServiceTest extends AbstractTestInit {

  @Autowired
  private PlayerService playerService;


  @Test
  void getPlayers() {
    List<Player> players = playerService.getPlayers().collectList().block();
    assertNotNull(players);
    assertFalse(players.isEmpty());
  }

  @Test
  void getPlayer() {
    Player player = playerService.getPlayer(387138L).block();
    assertNotNull(player);
    assertEquals(387138L, player.id());
    assertEquals("Jennifer HERMOSO", player.name());
    assertEquals(10, player.jerseyNumber());
    assertEquals("Forward", player.position());
    assertEquals("1990-05-09", player.dateOfBirth().toString());
  }

  @Test
  void getPlayerByName() {
    Player player = playerService.getPlayerByName("Jennifer HERMOSO").block();
    assertNotNull(player);
    assertEquals(387138L, player.id());
    assertEquals("Jennifer HERMOSO", player.name());
    assertEquals(10, player.jerseyNumber());
    assertEquals("Forward", player.position());
    assertEquals("1990-05-09", player.dateOfBirth().toString());
  }

  @Test
  void getPlayerByName_notFound() {
    Player player = playerService.getPlayerByName("inexistent name").block();
    assertNull(player);
  }
}
