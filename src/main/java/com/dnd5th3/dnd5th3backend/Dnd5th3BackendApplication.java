package com.dnd5th3.dnd5th3backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Dnd5th3BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Dnd5th3BackendApplication.class, args);
    }

}