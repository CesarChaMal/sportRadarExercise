package com.sportradar.exercise.scoring;

import com.sportradar.exercise.match.Score;

public interface Scorable {
    int getHomeScore();
    void setHomeScore(int homeScore);
    int getAwayScore();
    void setAwayScore(int awayScore);
    void updateScore(int homeScore, int awayScore);
    int getTotalScore();
}