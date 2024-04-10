package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;
import com.sportradar.exercise.observer.Observer;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.state.MatchStateManager;
import com.sportradar.exercise.strategy.ScoringStrategy;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import com.sportradar.exercise.strategy_functionall1.ScoringStrategiesFunctional1;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategyType;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.BiConsumer;

public class Match implements MatchInterface, Comparable<Match> {
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final long startTime;
    private final long creationTime;
    private MatchEventNotifier<MatchEvent> matchEventNotifier;
    private final MatchStateManager stateManager;
    private ScoringStrategy scoringStrategy;
    private BiConsumer<Match, int[]> scoringStrategyFunctional1;
    private ScoringStrategyType scoringStrategyFunctional2;
    private ScoringStrategyMode currentStrategyMode = ScoringStrategyMode.CLASSIC;

    private Match(Builder builder) {
        this.homeTeam = builder.homeTeam;
        this.awayTeam = builder.awayTeam;
        this.homeScore = builder.homeScore;
        this.awayScore = builder.awayScore;
        this.startTime = System.currentTimeMillis();
        this.creationTime = System.nanoTime();
        matchEventNotifier = new MatchEventNotifier<>();
        this.stateManager = new MatchStateManager(this, builder.state);
        this.scoringStrategy = builder.scoringStrategy;
        this.scoringStrategyFunctional1 = builder.scoringStrategyFunctional1;
        this.scoringStrategyFunctional2 = builder.scoringStrategyFunctional2;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setHomeScore(int homeScore) {
        if (homeScore < 0) throw new IllegalArgumentException("Score must be non-negative");
        this.homeScore = homeScore;
    }

    public void setAwayScore(int awayScore) {
        if (awayScore < 0) throw new IllegalArgumentException("Score must be non-negative");
        this.awayScore = awayScore;
    }

    @Override
    public void updateScore(int homeScore, int awayScore) {
        this.stateManager.handleScoreUpdate(homeScore, awayScore);
    }

    public int getTotalScore() {
        return this.homeScore + this.awayScore;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public void registerObserver(Observer o) {
        matchEventNotifier.registerObserver(o);
    }

    public void removeObserver(Observer o) {
        matchEventNotifier.removeObserver(o);
    }

    public void notifyObservers() {
        MatchEvent event = new MatchEvent(this);
        matchEventNotifier.notifyObservers(event);
    }

    @Override
    public MatchState getState() {
        return this.stateManager.getCurrentState();
    }

    @Override
    public void setState(MatchState newState) {
        this.stateManager.transitionState(newState);
        this.notifyObservers();
    }

    public ScoringStrategy getScoringStrategy() {
        return this.scoringStrategy;
    }

    public BiConsumer<Match, int[]> getScoringStrategyFunctional1() {
        return this.scoringStrategyFunctional1;
    }

    public ScoringStrategyType getScoringStrategyFunctional2() {
        return this.scoringStrategyFunctional2;
    }

    public void setScoringStrategy(ScoringStrategy strategy) {
        Objects.requireNonNull(strategy, "Scoring strategy must not be null");
        if (currentStrategyMode == ScoringStrategyMode.CLASSIC) {
            this.scoringStrategy = strategy;
            this.notifyObservers();
        } else {
            throw new IllegalArgumentException("Scoring strategy must be of type ScoringStrategy");
        }
    }

    public void setScoringStrategyFunctional1(BiConsumer<Match, int[]> strategy) {
        Objects.requireNonNull(strategy, "Scoring strategy must not be null");
        if (currentStrategyMode == ScoringStrategyMode.FUNCTIONAL1) {
            this.scoringStrategyFunctional1 = strategy;
            this.notifyObservers();
        } else {
            throw new IllegalArgumentException("Scoring strategy must be of type BiConsumer<Match, int[]>");
        }
    }

    @Override
    public void setScoringStrategyFunctional2(ScoringStrategyType strategy) {
        Objects.requireNonNull(strategy, "Scoring strategy must not be null");
        if (currentStrategyMode == ScoringStrategyMode.FUNCTIONAL2) {
            this.scoringStrategyFunctional2 = strategy;
            this.notifyObservers();
        } else {
            throw new IllegalArgumentException("Scoring strategy must be of type ScoringStrategyType");
        }
    }

    public ScoringStrategyMode getCurrentStrategyMode() {
        return currentStrategyMode;
    }


    public void setCurrentStrategyMode(ScoringStrategyMode mode) {
        this.currentStrategyMode = mode;
    }

    public static class Builder {
        private String homeTeam;
        private String awayTeam;
        private int homeScore = 0;
        private int awayScore = 0;
        private MatchState state;
        private ScoringStrategy scoringStrategy;
        private BiConsumer<Match, int[]> scoringStrategyFunctional1;
        private ScoringStrategyType scoringStrategyFunctional2 = ScoringStrategyType.DEFAULT;
        private ScoringStrategyMode strategyMode = ScoringStrategyMode.CLASSIC;
        private boolean isScoringStrategyModeSet = false;

        public Builder(String homeTeam, String awayTeam) {
            this.homeTeam = Objects.requireNonNull(homeTeam.strip(), "Home team must not be null");
            this.awayTeam = Objects.requireNonNull(awayTeam.strip(), "Away team must not be null");
            this.state = MatchState.forNotStartedState();
            this.scoringStrategy = ScoringStrategy.forDefaultScoringStrategy();
            this.scoringStrategyFunctional1 = ScoringStrategiesFunctional1.defaultScoringStrategy;
        }

        public Builder homeScore(int value) {
            if (value < 0) throw new IllegalArgumentException("Score must be non-negative");
            this.homeScore = value;
            return this;
        }

        public Builder awayScore(int value) {
            if (value < 0) throw new IllegalArgumentException("Score must be non-negative");
            this.awayScore = value;
            return this;
        }

        public Builder state(MatchState state) {
            this.state = state;
            return this;
        }

        public Builder scoringStrategy(ScoringStrategy strategy) {
            if (strategyMode == ScoringStrategyMode.CLASSIC) {
                this.scoringStrategy = strategy;
            } else {
                throw new IllegalArgumentException("Scoring strategy must be of type ScoringStrategy");
            }
            return this;
        }

        public Builder scoringStrategyFunctional1(BiConsumer<Match, int[]> strategy) {
            if (strategyMode == ScoringStrategyMode.FUNCTIONAL1) {
                this.scoringStrategyFunctional1 = strategy;
            } else {
                throw new IllegalArgumentException("Scoring strategy must be of type BiConsumer<Match, int[]>");
            }
            return this;
        }

        public Builder scoringStrategyFunctional2(ScoringStrategyType strategyType) {
            if (strategyMode == ScoringStrategyMode.FUNCTIONAL2) {
                this.scoringStrategyFunctional2 = strategyType;
            } else {
                throw new IllegalArgumentException("Scoring strategy must be of type ScoringStrategyType");
            }
            return this;
        }

        public Builder scoringStrategyMode(ScoringStrategyMode strategyMode) {
            ensureSingleStrategy();
            this.strategyMode = strategyMode;
            isScoringStrategyModeSet = true;
            return this;
        }

        private void ensureSingleStrategy() {
            if (isScoringStrategyModeSet) {
                throw new IllegalStateException("A scoring strategy mode has already been set.");
            }
        }

        public Match build() {
            Match match = new Match(this);
            match.setCurrentStrategyMode(strategyMode);
            return match;
        }
    }

    @Override
    public int compareTo(Match other) {
        return Long.compare(this.startTime, other.startTime);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        String formattedStartTime = formatter.format(Instant.ofEpochMilli(this.startTime));

        return String.format("Match[%s vs. %s: %d-%d (Start Time: %s)]", homeTeam, awayTeam, homeScore, awayScore, formattedStartTime);
    }
}
