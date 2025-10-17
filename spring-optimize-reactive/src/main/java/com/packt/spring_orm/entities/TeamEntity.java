package com.packt.spring_orm.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamEntity {
  @Id
  private Long id;
  private String name;
}
