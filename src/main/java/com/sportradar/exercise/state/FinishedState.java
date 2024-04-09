package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

import static java.lang.System.out;

public class FinishedState implements MatchState {
    @Override
    public void updateScore(Match match, int homeScore, int awayScore) {
        throw new UnsupportedOperationException("Match is finished. Scores cannot be updated.");
    }

    @Override
    public void finishMatch(Match match) {
        out.println("Match is already finished.");
    }

    @Override
    public boolean canUpdateScore() {
        return false;
    }
}
