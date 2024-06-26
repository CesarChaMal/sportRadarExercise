package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

public final class NotStartedState implements MatchState {
    @Override
    public void startMatch(Match match) {
        match.setState(MatchState.forInProgressState());
    }

    @Override
    public void finishMatch(Match match) {
        throw new UnsupportedOperationException("Cannot finish a match that hasn't started.");
    }

    @Override
    public void pauseMatch(Match match) {
        throw new UnsupportedOperationException("Cannot pause a match that hasn't started.");
    }

    @Override
    public void resumeMatch(Match match) {
        throw new UnsupportedOperationException("Cannot resume a match that hasn't started.");
    }

    @Override
    public boolean canUpdateScore() {
        return false;
    }

    @Override
    public boolean isValidTransition(MatchState newState) {
        return newState instanceof InProgressState;
    }
}
