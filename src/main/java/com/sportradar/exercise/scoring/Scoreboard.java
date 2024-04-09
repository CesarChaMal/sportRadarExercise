package com.sportradar.exercise.scoring;

import com.sportradar.exercise.analytics.MatchSummaryGenerator;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.MatchInterface;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard {
    private final List<MatchInterface> matches;
    private final MatchSummaryGenerator summaryGenerator;

    public Scoreboard() {
        this.matches = new ArrayList<>();
        this.summaryGenerator = new MatchSummaryGenerator();
    }

    public void startMatch(String homeTeam, String awayTeam) {
        matches.add(new Match.Builder(homeTeam, awayTeam).build());
    }

    public void updateScore(MatchInterface match, int homeScore, int awayScore) {
        match.updateScore(homeScore, awayScore);
    }

    public void finishMatch(MatchInterface match) {
        matches.remove(match);
    }

    //O(n log n
    public List<MatchInterface> getSummary() {
        return summaryGenerator.generateSummary(new ArrayList<>(matches));
    }

    public MatchInterface getMatch(String homeTeam, String awayTeam) {
        return matches.stream()
                .filter(match -> match.getHomeTeam().equals(homeTeam) && match.getAwayTeam().equals(awayTeam))
                .findFirst()
                .orElse(null);
    }
}
