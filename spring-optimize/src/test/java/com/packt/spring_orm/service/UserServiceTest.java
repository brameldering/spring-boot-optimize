package com.packt.spring_orm.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.NoSuchElementException;

import com.packt.spring_orm.entities.UserEntity;
import com.packt.spring_orm.records.User;
import com.packt.spring_orm.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = UserServiceTest.Initializer.class)
public class UserServiceTest extends AbstractIntegrationInit {

  @Autowired
  UserService userService;

  @Test
  public void testCreateUser() {
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("test");
    userEntity.setId(1L);

    User user = userService.createUser("test");

    assertEquals("test", user.username());
  }

  @Test
  public void testGetUsers() {
    userService.createUser("test1");
    userService.createUser("test2");
    List<User> users = userService.getUsers();
    assertThat(users, not(empty()));
  }

  @Test
  public void testGetUser() {
    User user1 = userService.createUser("test1");

    User user = userService.getUserById(user1.id());

    assertEquals("test1", user.username());
    assertEquals(user1.id(), user.id());
    assertEquals(user1, user);
  }

  @Test
  public void testGetUserNotFound() {
    assertThrows(NoSuchElementException.class, () -> userService.getUserById(888888L));
  }
}
