package com.sportradar.exercise.match;

public interface MatchInfo {
    Long getId();
    Team<?> getHomeTeam();
    Team<?> getAwayTeam();
}