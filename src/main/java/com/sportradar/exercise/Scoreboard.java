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

    //O(n^2
    public List<Match> getSummary() {
        List<Match> sortedMatches = new ArrayList<>();

        for (Match currentMatch : matches) {
            boolean added = false;
            for (int i = 0; i < sortedMatches.size(); i++) {
                Match sortedMatch = sortedMatches.get(i);
                if (currentMatch.getTotalScore() > sortedMatch.getTotalScore() || (currentMatch.getTotalScore() == sortedMatch.getTotalScore() && currentMatch.getCreationTime() > sortedMatch.getCreationTime())) {
                    sortedMatches.add(i, currentMatch);
                    added = true;
                    break;
                }
            }
            if (!added) {
                sortedMatches.add(currentMatch);
            }
        }

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
