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
    public boolean canUpdateScore() {
        return true;
    }
}
