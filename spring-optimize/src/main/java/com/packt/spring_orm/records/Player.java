package com.packt.spring_orm.records;

import java.time.LocalDate;

public record Player(Long id, String name, Integer jerseyNumber, String position, LocalDate dateOfBirth) {
}
