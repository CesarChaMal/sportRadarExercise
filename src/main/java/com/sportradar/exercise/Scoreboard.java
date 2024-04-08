package com.sportradar.exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Scoreboard {
    private final List<Match> matches;

    public Scoreboard() {
        this.matches = new ArrayList<>();
    }

    public void startMatch(String homeTeam, String awayTeam) {
        matches.add(new Match(homeTeam, awayTeam));
    }

    public void updateScore(Match match, int homeScore, int awayScore) {
        match.updateScore(homeScore, awayScore);
    }

    public void finishMatch(Match match) {
        matches.remove(match);
    }

    //O(n log n
    public List<Match> getSummary() {
        return Collections.unmodifiableList(matches.stream()
                .sorted(Comparator.comparingInt(Match::getTotalScore).reversed()
                .thenComparing(Match::getCreationTime, Comparator.reverseOrder()))
                .collect(Collectors.toList()));
    }

    public Match getMatch(String homeTeam, String awayTeam) {
        return matches.stream()
                .filter(match -> match.getHomeTeam().equals(homeTeam) && match.getAwayTeam().equals(awayTeam))
                .findFirst()
                .orElse(null);
    }
}
