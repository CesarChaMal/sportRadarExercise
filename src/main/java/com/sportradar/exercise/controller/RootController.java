package com.sportradar.exercise.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
            "application", "SportRadar Scoreboard API",
            "version", "v0.5.0-SNAPSHOT",
            "endpoints", Map.of(
                "matches", "/api/matches",
                "health", "/actuator/health",
                "h2-console", "/h2-console"
            )
        );
    }
}