package com.packt.spring_orm.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardEntity {
  @Id
  private Long id;

  private Optional<Long> albumId;

  private Long playerId;

  private Long ownerId;
}
