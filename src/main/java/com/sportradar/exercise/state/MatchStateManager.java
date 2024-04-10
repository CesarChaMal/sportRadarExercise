package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.strategy.ScoreApplier;

public class MatchStateManager {
    private MatchState currentState;
    private final Match match;

    public MatchStateManager(Match match, MatchState initialState) {
        this.match = match;
        this.currentState = initialState;
    }

    public void transitionState(MatchState newState) {
        if (currentState instanceof FinishedState && newState instanceof FinishedState) {
            throw new UnsupportedOperationException("Cannot finish an already finished match.");
        }
        if (currentState instanceof NotStartedState && newState instanceof FinishedState) {
            throw new UnsupportedOperationException("Cannot finish a match that hasn't started.");
        }
        currentState = newState;
    }

    public void handleScoreUpdate(int homeScore, int awayScore) {
        if (!currentState.canUpdateScore()) {
            throw new UnsupportedOperationException("Score update not allowed in current state: " + currentState.getClass().getSimpleName());
        }

        if (match.getScoringStrategy() instanceof ScoreApplier) {
            ((ScoreApplier) match.getScoringStrategy()).applyScore(match, homeScore, awayScore);
            match.notifyObservers();
        } else {
            throw new UnsupportedOperationException("Score update not allowed by the current scoring strategy.");
        }
    }

    public MatchState getCurrentState() {
        return currentState;
    }
}
