package com.packt.spring_orm.mappers;

import com.packt.spring_orm.records.User;
import com.packt.spring_orm.entities.UserEntity;

public class UserMapper {
    public static User map(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getUsername());
    }
}
