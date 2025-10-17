package com.packt.spring_orm.records;

import java.util.Optional;

public record Card(Long id, Optional<Album> album, Player player) {
}
