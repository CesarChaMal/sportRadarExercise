package com.sportradar.exercise.scoring;

public interface Scorable {
    int getHomeScore();
    int getAwayScore();
    void updateScore(int homeScore, int awayScore);
    int getTotalScore();
}