package com.sportradar.exercise.scoring;

import com.sportradar.exercise.match.EventType;
import com.sportradar.exercise.match.Score;

public interface Scorable {
    int getHomeScore();
    void setHomeScore(int homeScore);
    int getAwayScore();
    void setAwayScore(int awayScore);
    void updateScore(EventType eventTyp, int homeScore, int awayScore);
    int getTotalScore();
}