package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

public final class FinishedState implements MatchState {
    @Override
    public void startMatch(Match match) {
        throw new UnsupportedOperationException("Match is already finished.");
    }

    @Override
    public void finishMatch(Match match) {
        throw new UnsupportedOperationException("Match is already finished.");
    }

    @Override
    public void pauseMatch(Match match) {
        throw new UnsupportedOperationException("Match is already finished.");
    }

    @Override
    public void resumeMatch(Match match) {
        throw new UnsupportedOperationException("Match is already finished.");
    }

    @Override
    public boolean canUpdateScore() {
        return false;
    }

    public boolean isValidTransition(MatchState newState) {
        return false;
    }
}
