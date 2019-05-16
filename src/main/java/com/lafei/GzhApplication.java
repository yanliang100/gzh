package com.lafei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GzhApplication {
    public static void main(String[] args) {
        SpringApplication.run(GzhApplication.class, args);
    }

}

