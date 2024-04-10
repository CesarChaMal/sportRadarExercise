package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

public interface MatchState {
    void startMatch(Match match);
    void finishMatch(Match match);
    boolean canUpdateScore();
}
