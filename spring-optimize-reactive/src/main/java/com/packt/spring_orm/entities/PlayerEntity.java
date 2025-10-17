package com.packt.spring_orm.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name="players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {
  @Id
  private Long id;
  private Integer jerseyNumber;
  private String name;
  private String position;
  private LocalDate dateOfBirth;
  private Integer height;
  private Integer weight;

  private Long teamId;
}
