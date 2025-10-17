package com.packt.spring_orm.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
//import org.springframework.data.relational.core.mapping.Column;
//import org.springframework.data.relational.core.mapping.MappedCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "albums")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumEntity {

  @Id
  private Long id;

  private String title;

  private LocalDate expireDate;

//  @MappedCollection(idColumn = "album_id") // 'album_id' is the foreign key column in the 'cards' table
//  private List<CardEntity> cards;

  private Long ownerId; // Reference by ID instead of @ManyToOne
}
