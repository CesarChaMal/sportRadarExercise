package com.sportradar.exercise.strategy;

import com.sportradar.exercise.match.Match;

public class BasketballNormalTimeScoringStrategy implements ScoringStrategy, ScoreApplier {
    @Override
    public void calculateScore(Match match, int homeScore, int awayScore) {
        match.updateScore(homeScore, awayScore);
    }

    @Override
    public void applyScore(Match match, int homeScore, int awayScore) {
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
    }
}
