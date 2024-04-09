package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

public interface MatchState {
    void updateScore(Match match, int homeScore, int awayScore);
    void finishMatch(Match match);
    boolean canUpdateScore();
}
