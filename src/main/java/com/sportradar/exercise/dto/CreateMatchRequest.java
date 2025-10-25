package com.sportradar.exercise.dto;

public record CreateMatchRequest(String homeTeamName, String awayTeamName, String matchType) {
}