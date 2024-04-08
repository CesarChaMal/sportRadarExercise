package com.sportradar.exercise.match;

public interface MatchInterface {
    String getHomeTeam();
    String getAwayTeam();
    int getHomeScore();
    int getAwayScore();
    void updateScore(int homeScore, int awayScore);
    int getTotalScore();
    long getStartTime();
    long getCreationTime();
}
