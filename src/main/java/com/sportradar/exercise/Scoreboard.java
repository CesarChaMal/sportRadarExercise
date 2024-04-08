package com.sportradar.exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Scoreboard {
    private final List<Match> matches;

    public Scoreboard() {
        this.matches = new ArrayList<>();
    }

    public void startMatch(String homeTeam, String awayTeam) {
        matches.add(new Match(homeTeam, awayTeam));
    }

    //O(n log n
    public List<Match> getSummary() {
        List<Match> sortedMatches;
        sortedMatches = matches.stream()
                .sorted(Comparator.comparingInt(Match::getTotalScore).reversed()
                .thenComparing(Match::getCreationTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return Collections.unmodifiableList(sortedMatches);
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
