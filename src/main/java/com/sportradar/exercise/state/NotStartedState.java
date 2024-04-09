package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

import static java.lang.System.out;

public class NotStartedState implements MatchState {
    @Override
    public void updateScore(Match match, int homeScore, int awayScore) {
        throw new UnsupportedOperationException("Match not started. Scores cannot be updated.");
    }

    @Override
    public void finishMatch(Match match) {
        out.println("Cannot finish a match that hasn't started.");
    }

    @Override
    public boolean canUpdateScore() {
        return false;
    }
}
