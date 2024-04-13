package com.sportradar.exercise.state;

import com.sportradar.exercise.match.EventType;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.strategy.ScoreApplier;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategiesFunctional2;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategyType;

import java.util.function.BiConsumer;

import static com.sportradar.exercise.state.MatchState.*;

public class MatchStateManager {
    private MatchState currentState;
    private final Match match;

    public MatchStateManager(Match match, MatchState initialState) {
        this.match = match;
        this.currentState = initialState;
    }

    public void transitionState(MatchState newState) {
        if (!currentState.isValidTransition(newState)) {
            throw new UnsupportedOperationException("Invalid state transition from "
                    + currentState.getClass().getSimpleName() + " to " + newState.getClass().getSimpleName());
        }
        currentState = newState;
        match.notifyObservers(new MatchChangeEvent(match, getEventTypeForState(newState)));
    }

    private EventType getEventTypeForState(MatchState state) {
        if (state.equals(IN_PROGRESS)) {
            return EventType.MATCH_STARTED;
        } else if (state.equals(FINISHED)) {
            return EventType.MATCH_FINISHED;
        } else if (state.equals(IN_PAUSED)) {
            return EventType.MATCH_PAUSED;
        }
        return EventType.UNKNOWN;
    }

    public void handleScoreUpdate(int homeScore, int awayScore) {
        if (!currentState.canUpdateScore()) {
            throw new UnsupportedOperationException("Score update not allowed in current state: " + currentState.getClass().getSimpleName());
        }

        switch (match.getStrategyMode()) {
            case CLASSIC:
                applyScoringStrategy(homeScore, awayScore);
                break;
            case FUNCTIONAL1:
                applyScoringStrategyFunctional1(homeScore, awayScore);
                break;
            case FUNCTIONAL2:
                applyScoringStrategyFunctional2(homeScore, awayScore);
                break;
            default:
                throw new UnsupportedOperationException("Unknown scoring strategy mode.");
        }
    }

    private void applyScoringStrategy(int homeScore, int awayScore) {
        if (match.getScoringStrategy() instanceof ScoreApplier) {
            ((ScoreApplier) match.getScoringStrategy()).applyScore(match, homeScore, awayScore);
            match.notifyObservers(new MatchChangeEvent( match, EventType.SCORE_UPDATE));
        } else {
            throw new UnsupportedOperationException("Score update not allowed by the current scoring strategy.");
        }
    }

    private void applyScoringStrategyFunctional1(int homeScore, int awayScore) {
        BiConsumer<Match, int[]> scoringStrategy = match.getScoringStrategyFunctional1();
        if (scoringStrategy != null) {
            scoringStrategy.accept(match, new int[]{homeScore, awayScore});
            match.notifyObservers(new MatchChangeEvent( match, EventType.SCORE_UPDATE));
        } else {
            throw new UnsupportedOperationException("Score update not allowed by the current scoring strategy.");
        }
    }

    private void applyScoringStrategyFunctional2(int homeScore, int awayScore) {
        ScoringStrategyType scoringStrategyType = match.getScoringStrategyFunctional2();
        if (scoringStrategyType != null) {
            BiConsumer<Match, int[]> scoringStrategy = ScoringStrategiesFunctional2.getStrategy(scoringStrategyType);
            if (scoringStrategy != null) {
                scoringStrategy.accept(match, new int[]{homeScore, awayScore});
                match.notifyObservers(new MatchChangeEvent(match, EventType.MATCH_NO_STARTED));
            } else {
                throw new UnsupportedOperationException("Scoring strategy not found for type: " + scoringStrategyType);
            }
        } else {
            throw new UnsupportedOperationException("No scoring strategy type set for the match.");
        }
    }

    public MatchState getCurrentState() {
        return currentState;
    }
}
