package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
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
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Match implements MatchInterface, Comparable<Match> {
    private final Team<?> homeTeam;
    private final Team<?> awayTeam;
    private final Score score;
    private final long startTime;
    private final long creationTime;
    private final MatchEventNotifier<MatchChangeEvent> matchEventNotifier;
    private final MatchStateManager stateManager;
    private ScoringStrategy scoringStrategy;
    private BiConsumer<Match, int[]> scoringStrategyFunctional1;
    private ScoringStrategyType scoringStrategyFunctional2;
    private ScoringStrategyMode strategyMode;
    private EventManager eventManager;
    private boolean enableValidationOfStrategyMode;

    protected Match(Builder builder) {
        this.homeTeam = builder.homeTeam;
        this.awayTeam = builder.awayTeam;
        this.score = new Score(builder.homeScore, builder.awayScore);
        this.startTime = System.currentTimeMillis();
        this.creationTime = System.nanoTime();
        matchEventNotifier = new MatchEventNotifier<>();
        this.stateManager = new MatchStateManager(this, builder.state);
        this.scoringStrategy = builder.scoringStrategy;
        this.strategyMode = builder.strategyMode;
        this.scoringStrategyFunctional1 = builder.scoringStrategyFunctional1;
        this.scoringStrategyFunctional2 = builder.scoringStrategyFunctional2;
        this.eventManager = (EventManager) builder.eventManagerFactory.apply(this);
    }

    public Team<?> getHomeTeam() {
        return homeTeam;
    }

    public Team<?> getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return score.getHomeScore();
    }

    public int getAwayScore() {
        return this.score.getAwayScore();
    }

    public void setHomeScore(int homeScore) {
        if (homeScore < 0) throw new IllegalArgumentException("Score must be non-negative");
        score.setHomeScore(homeScore);
//        addGoalEvent(homeTeam.getRoster()).;
//        notifyObservers(new MatchChangeEvent(this));
    }

    public void setAwayScore(int awayScore) {
        if (awayScore < 0) throw new IllegalArgumentException("Score must be non-negative");
        score.setAwayScore(awayScore);
//        notifyObservers(new MatchChangeEvent(this));
    }

    @Override
    public void updateScore(int homeScore, int awayScore) {
        this.stateManager.handleScoreUpdate(homeScore, awayScore);
        this.eventManager.addScoreUpdateEvent();
        notifyObservers(new MatchChangeEvent(this));
    }

    public int getTotalScore() {
        return this.score.getTotalScore();
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public void notifyObservers(MatchChangeEvent matchChangeEvent) {
        MatchChangeEvent event = new MatchChangeEvent(this);
        matchEventNotifier.notifyObservers(event);
    }

    @Override
    public MatchState getState() {
        return this.stateManager.getCurrentState();
    }

    @Override
    public void setState(MatchState newState) {
        this.stateManager.transitionState(newState);
//        notifyObservers(new MatchChangeEvent(this));
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
        if (strategyMode == ScoringStrategyMode.CLASSIC) {
            this.scoringStrategy = strategy;
//            this.notifyObservers(new MatchChangeEvent(this));
        } else {
            throw new IllegalArgumentException("Scoring strategy must be of type ScoringStrategy");
        }
    }

    public void setScoringStrategyFunctional1(BiConsumer<Match, int[]> strategy) {
        Objects.requireNonNull(strategy, "Scoring strategy must not be null");
        if (strategyMode == ScoringStrategyMode.FUNCTIONAL1) {
            this.scoringStrategyFunctional1 = strategy;
//            this.notifyObservers(new MatchChangeEvent(this));
        } else {
            throw new IllegalArgumentException("Scoring strategy must be of type BiConsumer<Match, int[]>");
        }
    }

    @Override
    public void setScoringStrategyFunctional2(ScoringStrategyType strategy) {
        Objects.requireNonNull(strategy, "Scoring strategy must not be null");
        if (strategyMode == ScoringStrategyMode.FUNCTIONAL2) {
            this.scoringStrategyFunctional2 = strategy;
//            this.notifyObservers(new MatchChangeEvent(this));
        } else {
            throw new IllegalArgumentException("Scoring strategy must be of type ScoringStrategyType");
        }
    }

    public ScoringStrategyMode getStrategyMode() {
        return strategyMode;
    }

    public void setStrategyMode(ScoringStrategyMode mode) {
        if (enableValidationOfStrategyMode) {
            if (strategyMode != null && strategyMode != mode) {
                throw new IllegalStateException("A scoring strategy mode has already been set and cannot be changed.");
            }
        }
        this.strategyMode = mode;
    }

    public void addEvent(EventType eventType, List<? extends Player> involvedPlayers) {
        eventManager.addEvent(eventType, involvedPlayers);
    }

    public void registerObserver(Observer<MatchChangeEvent> observer) {
        eventManager.registerObserver(observer);
    }

    public void removeObserver(Observer<MatchChangeEvent> observer) {
        eventManager.removeObserver(observer);
    }

    public List<MatchEvent<?>> getEvents() {
        return eventManager.getEvents();
    }

    protected EventManager getEventManager() {
        return eventManager;
    }

    protected Score getScore() {
        return this.score;
    }

    protected void incrementHomeScore(int increment) {
        this.score.setHomeScore(this.score.getHomeScore() + increment);
    }

    protected void incrementAwayScore(int increment) {
        this.score.setAwayScore(this.score.getAwayScore() + increment);
    }

    public boolean isEnableValidationOfStrategyMode() {
        return enableValidationOfStrategyMode;
    }

    public void setEnableValidationOfStrategyMode(boolean enableValidationOfStrategyMode) {
        this.enableValidationOfStrategyMode = enableValidationOfStrategyMode;
    }

    public abstract static class Builder<T extends Builder<T>> {
        private Team<?> homeTeam;
        private Team<?> awayTeam;
        private int homeScore = 0;
        private int awayScore = 0;
        private MatchState state;
        private ScoringStrategy scoringStrategy;
        private BiConsumer<Match, int[]> scoringStrategyFunctional1;
        private ScoringStrategyType scoringStrategyFunctional2;
        private ScoringStrategyMode strategyMode = ScoringStrategyMode.CLASSIC;
        private boolean isScoringStrategyModeSet = false;
        private Function<Match, EventManager> eventManagerFactory;

        public Builder(Team<?> homeTeam, Team<?> awayTeam) {
            this.homeTeam = Objects.requireNonNull(homeTeam, "Home team must not be null");
            this.awayTeam = Objects.requireNonNull(awayTeam, "Away team must not be null");
            this.state = MatchState.forNotStartedState();
            this.scoringStrategy = ScoringStrategy.forDefaultScoringStrategy();
            this.scoringStrategyFunctional1 = ScoringStrategiesFunctional1.defaultScoringStrategy;
            this.scoringStrategyFunctional2 = ScoringStrategyType.DEFAULT;
        }

        public T homeTeam(Team<?> homeTeam) {
            this.homeTeam = homeTeam;
            return self();
        }

        public T awayTeam(Team<?> awayTeam) {
            this.awayTeam = awayTeam;
            return self();
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
//            ensureSingleStrategy();
            this.strategyMode = strategyMode;
            isScoringStrategyModeSet = true;
            return this;
        }

        private void ensureSingleStrategy() {
            if (isScoringStrategyModeSet) {
                throw new IllegalStateException("A scoring strategy mode has already been set.");
            }
        }

        public T eventManagerFactory(Function<Match, EventManager> eventManagerFactory) {
            this.eventManagerFactory = eventManagerFactory;
            return self();
        }

        protected abstract T self();

        public abstract Match build();
    }

    @Override
    public int compareTo(Match other) {
        return Long.compare(this.startTime, other.startTime);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        String formattedStartTime = formatter.format(Instant.ofEpochMilli(this.startTime));

        return String.format("Match[%s vs. %s: %d-%d (Start Time: %s)]", homeTeam, awayTeam, this.getHomeScore(), this.getAwayScore(), formattedStartTime);
    }
}
