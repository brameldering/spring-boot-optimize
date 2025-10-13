package com.packt.spring_orm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringOrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringOrmApplication.class, args);
	}

}
