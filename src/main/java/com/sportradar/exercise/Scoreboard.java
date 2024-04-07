package com.sportradar.exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.out;

public class Scoreboard {
    private final List<Match> matches;

    public Scoreboard() {
        this.matches = new ArrayList<>();
    }

    public void startMatch(String homeTeam, String awayTeam) {
        out.printf("Starting match between %s and %s%n", homeTeam, awayTeam);
        matches.add(new Match(homeTeam, awayTeam));
    }

    public List<Match> getSummary() {
        return  Collections.unmodifiableList(matches);
    }

    public Match getMatch(String homeTeam, String awayT) {
        for (Match match : matches) {
            if (match.getHomeTeam().equals(homeTeam) && match.getAwayTeam().equals(awayT)) {
                return match;
            }
        }
        return null;
    }

    public void updateScore(Match match, int homeScore, int awayScore) {
        match.updateScore(homeScore, awayScore);
    }

    public void finishMatch(Match match) {
        matches.remove(match);
    }
}
