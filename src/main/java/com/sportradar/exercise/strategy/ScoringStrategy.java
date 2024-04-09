package com.sportradar.exercise.strategy;

import com.sportradar.exercise.match.Match;

public interface ScoringStrategy {
    void calculateScore(Match match, int homeScore, int awayScore);
    
    // Static factory methods
    static ScoringStrategy forFootballNormalTime() {
        return new FootballNormalTimeScoringStrategy();
    }
    
    static ScoringStrategy forFootballExtraTime() {
        return new FootballExtraTimeScoringStrategy();
    }
    
    static ScoringStrategy forBasketballNormalTime() {
        return new BasketballNormalTimeScoringStrategy();
    }

    static ScoringStrategy forBasketballExtraTime() {
        return new BasketballExtraTimeScoringStrategy();
    }
}
