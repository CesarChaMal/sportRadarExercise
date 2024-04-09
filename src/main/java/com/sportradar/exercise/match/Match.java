package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;
import com.sportradar.exercise.observer.Observer;
import com.sportradar.exercise.state.FinishedState;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.state.NotStartedState;
import com.sportradar.exercise.strategy.ScoringStrategy;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Match implements MatchInterface, Comparable<Match> {
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private final long startTime;
    private final long creationTime;
    private MatchEventNotifier<MatchEvent> matchEventNotifier;
    private MatchState state;
    private ScoringStrategy scoringStrategy;

    private Match(Builder builder) {
        this.homeTeam = builder.homeTeam;
        this.awayTeam = builder.awayTeam;
        this.homeScore = builder.homeScore;
        this.awayScore = builder.awayScore;
        this.startTime = System.currentTimeMillis();
        this.creationTime = System.nanoTime();
        matchEventNotifier = new MatchEventNotifier<>();
        this.state = builder.state;
        this.scoringStrategy = builder.scoringStrategy;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public void updateScore(int homeScore, int awayScore) {
        if (this.state.canUpdateScore()) {
            this.homeScore = homeScore;
            this.awayScore = awayScore;
            notifyObservers();
        } else {
            throw new UnsupportedOperationException("Score update not allowed in the current state: " + this.state.getClass().getSimpleName());
        }
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
        return this.state ;
    }

    @Override
    public void setState(MatchState newState) {
        if (this.state instanceof FinishedState && newState instanceof FinishedState) {
            throw new UnsupportedOperationException("Cannot finish an already finished match.");
        }
        if (this.state instanceof NotStartedState && newState instanceof FinishedState) {
            throw new UnsupportedOperationException("Cannot finish a match that hasn't started.");
        }
        this.state = newState;
        notifyObservers();
    }

    public ScoringStrategy getScoringStrategy() {
        return scoringStrategy;
    }

    public void setScoringStrategy(ScoringStrategy strategy) {
        this.scoringStrategy = strategy;
    }

    public static class Builder {
        private String homeTeam;
        private String awayTeam;
        private int homeScore = 0;
        private int awayScore = 0;
        private MatchState state;
        private ScoringStrategy scoringStrategy;

        public Builder(String homeTeam, String awayTeam) {
            this.homeTeam = Objects.requireNonNull(homeTeam.strip(), "Home team must not be null");
            this.awayTeam = Objects.requireNonNull(awayTeam.strip(), "Away team must not be null");
            this.state = new NotStartedState();
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
            this.scoringStrategy = strategy;
            return this;
        }

        public Match build() {
            return new Match(this);
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
