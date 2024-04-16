package com.sportradar.exercise.match;

import java.util.concurrent.atomic.AtomicInteger;

public class Score {
    private final AtomicInteger homeScore = new AtomicInteger();
    private final AtomicInteger awayScore = new AtomicInteger();

    public Score() {
        this(0, 0);
    }

    public Score(int homeScore, int awayScore) {
        this.homeScore.set(homeScore);
        this.awayScore.set(awayScore);
    }

    public int getHomeScore() {
        return homeScore.get();
    }

    public void setHomeScore(int homeScore) {
        if (homeScore < 0) {
            throw new IllegalArgumentException("Score must be non-negative");
        }
        this.homeScore.set(homeScore);
    }

    public int getAwayScore() {
        return awayScore.get();
    }

    public void setAwayScore(int awayScore) {
        if (awayScore < 0) {
            throw new IllegalArgumentException("Score must be non-negative");
        }
        this.awayScore.set(awayScore);
    }

    public void incrementHomeScore(int increment) {
        if (increment < 0) {
            throw new IllegalArgumentException("Increment must be non-negative");
        }
        this.homeScore.addAndGet(increment);
    }

    public void incrementAwayScore(int increment) {
        if (increment < 0) {
            throw new IllegalArgumentException("Increment must be non-negative");
        }
        this.awayScore.addAndGet(increment);
    }

    public void updateScores(int homeScore, int awayScore) {
        setHomeScore(homeScore);
        setAwayScore(awayScore);
    }

    public int getTotalScore() {
        return homeScore.get() + awayScore.get();
    }

    @Override
    public String toString() {
        return "Score{" +
                "homeScore=" + homeScore.get() +
                ", awayScore=" + awayScore.get() +
                '}';
    }
}
