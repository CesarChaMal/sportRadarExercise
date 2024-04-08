package com.sportradar.exercise.match;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Match  implements MatchInterface {
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private final long startTime;
    private final long creationTime;

    private Match(Builder builder) {
        this.homeTeam = builder.homeTeam;
        this.awayTeam = builder.awayTeam;
        this.homeScore = builder.homeScore;
        this.awayScore = builder.awayScore;
        startTime = System.currentTimeMillis();
        creationTime = System.nanoTime();
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
        this.homeScore = homeScore;
        this.awayScore = awayScore;
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

    public static class Builder {
        private String homeTeam;
        private String awayTeam;
        private int homeScore = 0;
        private int awayScore = 0;

        public Builder(String homeTeam, String awayTeam) {
            this.homeTeam = Objects.requireNonNull(homeTeam.strip(), "Home team must not be null");
            this.awayTeam = Objects.requireNonNull(awayTeam.strip(), "Away team must not be null");
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

        public Match build() {
            return new Match(this);
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        String formattedStartTime = formatter.format(Instant.ofEpochMilli(this.startTime));

        return String.format("Match[%s vs. %s: %d-%d (Start Time: %s)]", homeTeam, awayTeam, homeScore, awayScore, formattedStartTime);
    }
}