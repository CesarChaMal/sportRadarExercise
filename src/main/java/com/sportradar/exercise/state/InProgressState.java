package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

public final class InProgressState implements MatchState {
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
        match.setState(MatchState.forInPauseState());
    }

    @Override
    public void resumeMatch(Match match) {
        throw new UnsupportedOperationException("Match is already in progress.");
    }

    @Override
    public boolean canUpdateScore() {
        return true;
    }

    @Override
    public boolean isValidTransition(MatchState newState) {
        return newState instanceof FinishedState || newState instanceof InPauseState;
    }
}
