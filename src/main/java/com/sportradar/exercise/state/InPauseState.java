package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

public final class InPauseState implements MatchState {
    @Override
    public void startMatch(Match match) {
        throw new UnsupportedOperationException("Match is already started.");
    }

    @Override
    public void finishMatch(Match match) {
        match.setState(MatchState.forFinishedState());
    }

    @Override
    public void pauseMatch(Match match) {
        throw new UnsupportedOperationException("Match is already paused.");
    }

    @Override
    public void resumeMatch(Match match) {
        match.setState(MatchState.forInProgressState());
    }

    @Override
    public boolean canUpdateScore() {
        return true;
    }

    @Override
    public boolean isValidTransition(MatchState newState) {
        return newState instanceof InProgressState;
    }
}
