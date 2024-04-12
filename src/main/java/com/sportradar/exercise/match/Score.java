package com.sportradar.exercise.match;

public class Score {
    private int homeScore;
    private int awayScore;

    public Score() {
        this(0, 0);
    }

    public Score(int homeScore, int awayScore) {
        setHomeScore(homeScore);
        setAwayScore(awayScore);
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        if (homeScore < 0) {
            throw new IllegalArgumentException("Score must be non-negative");
        }
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        if (awayScore < 0) {
            throw new IllegalArgumentException("Score must be non-negative");
        }
        this.awayScore = awayScore;
    }

    public void incrementHomeScore(int increment) {
        if (increment < 0) {
            throw new IllegalArgumentException("Increment must be non-negative");
        }
        this.homeScore += increment;
    }

    public void incrementAwayScore(int increment) {
        if (increment < 0) {
            throw new IllegalArgumentException("Increment must be non-negative");
        }
        this.awayScore += increment;
    }

    public void updateScores(int homeScore, int awayScore) {
        setHomeScore(homeScore);
        setAwayScore(awayScore);
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }

    @Override
    public String toString() {
        return "Score{" +
                "homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                '}';
    }
}
