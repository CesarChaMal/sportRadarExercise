package com.sportradar.exercise.state;

import com.sportradar.exercise.match.EventType;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.strategy.ScoreApplier;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategiesFunctional2;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategyType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

import static com.sportradar.exercise.state.MatchState.*;

public class MatchStateManager {
    private MatchState currentState;
    private final Match match;
    private final Map<ScoringStrategyMode, BiConsumer<Integer, Integer>> scoringStrategies = new EnumMap<>(ScoringStrategyMode.class);
    private static final Map<MatchState, EventType> stateToEventTypeMap = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    static {
        stateToEventTypeMap.put(NOT_STARTED, EventType.MATCH_NO_STARTED);
        stateToEventTypeMap.put(IN_PROGRESS, EventType.MATCH_STARTED);
        stateToEventTypeMap.put(FINISHED, EventType.MATCH_FINISHED);
        stateToEventTypeMap.put(IN_PAUSED, EventType.MATCH_PAUSED);
        stateToEventTypeMap.put(null, EventType.UNKNOWN);
    }

    public MatchStateManager(Match match, MatchState initialState) {
        this.match = match;
        this.currentState = initialState;
        initializeScoringStrategies();
    }

    public void transitionState(MatchState newState) {
        lock.lock();
        try {
            if (!currentState.isValidTransition(newState)) {
                String error = String.format("Invalid state transition from %s to %s for match ID %s",
                        currentState, newState, match.getId());
                throw new UnsupportedOperationException(error);
            }
            currentState = newState;
            match.notifyObservers(new MatchChangeEvent(match, getEventTypeForState(newState)));
        } finally {
            lock.unlock();
        }
    }

    private EventType getEventTypeForState(MatchState state) {
        return stateToEventTypeMap.getOrDefault(state, EventType.UNKNOWN);
    }

/*
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
*/

    private void initializeScoringStrategies() {
        scoringStrategies.put(ScoringStrategyMode.CLASSIC, this::applyScoringStrategy);
        scoringStrategies.put(ScoringStrategyMode.FUNCTIONAL1, this::applyScoringStrategyFunctional1);
        scoringStrategies.put(ScoringStrategyMode.FUNCTIONAL2, this::applyScoringStrategyFunctional2);
    }

    public void handleScoreUpdate(int homeScore, int awayScore) {
        lock.lock();
        try {
            if (!currentState.canUpdateScore()) {
                throw new UnsupportedOperationException("Score update not allowed in current state: " + currentState.getClass().getSimpleName());
            }
            scoringStrategies.getOrDefault(match.getStrategyMode(),
                            (h, a) -> { throw new UnsupportedOperationException("Unknown scoring strategy mode."); })
                    .accept(homeScore, awayScore);
    /*
            switch (match.getStrategyMode()) {
                case CLASSIC -> applyScoringStrategy(homeScore, awayScore);
                case FUNCTIONAL1 -> applyScoringStrategyFunctional1(homeScore, awayScore);
                case FUNCTIONAL2 -> applyScoringStrategyFunctional2(homeScore, awayScore);
                default -> throw new UnsupportedOperationException("Unknown scoring strategy mode.");
            }
    */
        } finally {
            lock.unlock();
        }
    }

    private void applyScoringStrategy(int homeScore, int awayScore) {
/*
        if (match.getScoringStrategy() instanceof ScoreApplier) {
            ((ScoreApplier) match.getScoringStrategy()).applyScore(match, homeScore, awayScore);
            match.notifyObservers(new MatchChangeEvent(match, EventType.SCORE_UPDATE));
        } else {
            throw new UnsupportedOperationException("Score update not allowed by the current scoring strategy.");
        }
*/
        if (match.getScoringStrategy() instanceof ScoreApplier scoreApplier) {
            scoreApplier.applyScore(match, homeScore, awayScore);
            match.notifyObservers(new MatchChangeEvent(match, EventType.SCORE_UPDATE));
        } else {
            throw new UnsupportedOperationException("Score update not allowed by the current scoring strategy.");
        }
    }

    private void applyScoringStrategyFunctional1(int homeScore, int awayScore) {
        BiConsumer<Match, int[]> scoringStrategy = match.getScoringStrategyFunctional1();
        if (scoringStrategy != null) {
            scoringStrategy.accept(match, new int[]{homeScore, awayScore});
            match.notifyObservers(new MatchChangeEvent(match, EventType.SCORE_UPDATE));
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
