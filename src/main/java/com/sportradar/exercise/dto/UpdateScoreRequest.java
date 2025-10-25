package com.sportradar.exercise.dto;

import jakarta.validation.constraints.Min;

public record UpdateScoreRequest(
    @Min(value = 0, message = "Home score must be non-negative")
    int homeScore,
    
    @Min(value = 0, message = "Away score must be non-negative")
    int awayScore
) {}