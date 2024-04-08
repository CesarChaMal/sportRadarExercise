package com.sportradar.exercise;

import java.util.*;

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
        Comparator<Match> comparator = new Comparator<Match>() {
            @Override
            public int compare(Match m1, Match m2) {
                int scoreCompare = Integer.compare(m2.getTotalScore(), m1.getTotalScore());
                if (scoreCompare != 0) {
                    return scoreCompare;
                }
                return Long.compare(m2.getCreationTime(), m1.getCreationTime());
            }
        };

        Set<Match> sortedUniqueMatches = new TreeSet<>(comparator);

        for (Match currentMatch : matches)
            sortedUniqueMatches.add(currentMatch);

        out.println("After sorting:");
        sortedUniqueMatches.forEach(out::println);

        return Collections.unmodifiableList(new ArrayList<>(sortedUniqueMatches));
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
