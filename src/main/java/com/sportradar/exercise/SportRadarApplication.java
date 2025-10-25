package com.sportradar.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SportRadarApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SportRadarApplication.class, args);
    }
}