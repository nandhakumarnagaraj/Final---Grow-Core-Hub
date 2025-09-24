package com.growcorehub.version1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Version1Application {

    public static void main(String[] args) {
        SpringApplication.run(Version1Application.class, args);
    }
}
