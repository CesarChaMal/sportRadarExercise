package com.sportradar.exercise;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class Match {
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final long startTime;
    private final long creationTime;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = Objects.requireNonNull(homeTeam.strip(), "Home team must not be null");
        this.awayTeam = Objects.requireNonNull(awayTeam.strip(), "Away team must not be null");
        if (this.homeTeam.isEmpty() || this.awayTeam.isEmpty()) {
            throw new IllegalArgumentException("Team names must not be empty");
        }
        this.homeScore = 0;
        this.awayScore = 0;
        startTime = System.currentTimeMillis();
        creationTime = System.nanoTime();
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

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        String formattedStartTime = formatter.format(Instant.ofEpochMilli(this.startTime));

        return String.format("Match[%s vs. %s: %d-%d (Start Time: %s)]", homeTeam, awayTeam, homeScore, awayScore, formattedStartTime);
    }
}
