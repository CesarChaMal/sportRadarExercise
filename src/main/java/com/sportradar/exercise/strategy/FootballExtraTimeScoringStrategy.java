package com.sportradar.exercise.strategy;

import com.sportradar.exercise.match.EventType;
import com.sportradar.exercise.match.Match;

public class FootballExtraTimeScoringStrategy implements ScoringStrategy, ScoreApplier {
    @Override
    public void calculateScore(Match match, int homeScore, int awayScore) {
        int currentHomeScore = match.getHomeScore();
        int currentAwayScore = match.getAwayScore();
        match.updateScore(EventType.SCORE_UPDATE,currentHomeScore + homeScore, currentAwayScore + awayScore);
    }

    @Override
    public void applyScore(Match match, int homeScore, int awayScore) {
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
    }
}
