package com.packt.footballnative;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {
  @GetMapping
  public List<String> getTeams() {
    return List.of("Spain", "Zambia", "Brazil");
  }
}
