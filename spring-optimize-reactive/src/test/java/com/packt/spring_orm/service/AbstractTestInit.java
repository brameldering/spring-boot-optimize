package com.packt.spring_orm.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.BeforeAll;

/**
 * Abstract base class for integration tests using Testcontainers and Spring Boot.
 * It sets up a PostgreSQL Testcontainer and configures the Spring environment.
 */
@SpringBootTest
@Testcontainers
// Note: We use the local Initializer class defined below
@ContextConfiguration(initializers = AbstractTestInit.Initializer.class)
public abstract class AbstractTestInit {

  // 1. Static container instance
  protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.8")
      .withDatabaseName("football")
      .withUsername("football")
      .withPassword("football");

  // 2. Initializer class to apply container properties to Spring environment
  protected static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
              "spring.flyway.url=" + postgreSQLContainer.getJdbcUrl(),
              "spring.flyway.user=" + postgreSQLContainer.getUsername(),
              "spring.flyway.password=" + postgreSQLContainer.getPassword(),
              "spring.r2dbc.url=" + postgreSQLContainer.getJdbcUrl().replace("jdbc:", "r2dbc:"),
              "spring.r2dbc.username=" + postgreSQLContainer.getUsername(),
              "spring.r2dbc.password=" + postgreSQLContainer.getPassword())
          .applyTo(configurableApplicationContext.getEnvironment());
    }
  }

  // 3. Start the container before all tests in any subclass
  @BeforeAll
  public static void startContainer() {
    postgreSQLContainer.start();
  }
}