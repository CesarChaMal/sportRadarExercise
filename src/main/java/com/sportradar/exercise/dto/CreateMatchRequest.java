package com.sportradar.exercise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateMatchRequest(
    @NotBlank(message = "Home team name is required")
    String homeTeamName,
    
    @NotBlank(message = "Away team name is required")
    String awayTeamName,
    
    @NotBlank(message = "Match type is required")
    @Pattern(regexp = "FOOTBALL|BASKETBALL", message = "Match type must be FOOTBALL or BASKETBALL")
    String matchType
) {}