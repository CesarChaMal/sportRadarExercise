package com.sportradar.exercise.dto;

public record MatchResponse(Long id, String homeTeamName, String awayTeamName, int homeScore, int awayScore, String status) {
}