package com.sportradar.exercise.strategy;

import com.sportradar.exercise.match.Match;

public class ExtraTimeScoringStrategy implements ScoringStrategy, ScoreApplier {
    @Override
    public void calculateScore(Match match, int homeScore, int awayScore) {
        int currentHomeScore = match.getHomeScore();
        int currentAwayScore = match.getAwayScore();
        match.updateScore(currentHomeScore + homeScore, currentAwayScore + awayScore);
    }

    @Override
    public void applyScore(Match match, int homeScore, int awayScore) {
        match.setHomeScore(match.getHomeScore() + homeScore);
        match.setAwayScore(match.getAwayScore() + awayScore);
    }
}
