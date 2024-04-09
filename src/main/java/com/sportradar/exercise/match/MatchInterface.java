package com.sportradar.exercise.match;

import com.sportradar.exercise.state.MatchState;

public interface MatchInterface {
    String getHomeTeam();
    String getAwayTeam();
    int getHomeScore();
    int getAwayScore();
    void updateScore(int homeScore, int awayScore);
    int getTotalScore();
    long getStartTime();
    long getCreationTime();
    MatchState getState();
    void setState(MatchState state);
}
