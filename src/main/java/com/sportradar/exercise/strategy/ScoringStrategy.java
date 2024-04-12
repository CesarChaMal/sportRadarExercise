package com.sportradar.exercise.strategy;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.state.FinishedState;
import com.sportradar.exercise.state.InProgressState;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.state.NotStartedState;

public interface ScoringStrategy {
    void calculateScore(Match match, int homeScore, int awayScore);

    ScoringStrategy DEFAULT_SCORING_STRATEGY = new DefaultScoringStrategy();
    ScoringStrategy FOOTBALL_NORMAL_TIME = new FootballNormalTimeScoringStrategy();
    ScoringStrategy FOOTBALL_EXTRA_TIME = new FootballExtraTimeScoringStrategy();
    ScoringStrategy BASKETBALL_NORMAL_TIME = new BasketballNormalTimeScoringStrategy();
    ScoringStrategy BASKETBALLEXTRA_TIME = new BasketballExtraTimeScoringStrategy();

    static ScoringStrategy forDefaultScoringStrategy() {
        return DEFAULT_SCORING_STRATEGY;
    }

    static ScoringStrategy forFootballNormalTime() {
        return FOOTBALL_NORMAL_TIME;
    }
    
    static ScoringStrategy forFootballExtraTime() {
        return FOOTBALL_EXTRA_TIME;
    }
    
    static ScoringStrategy forBasketballNormalTime() {
        return BASKETBALL_NORMAL_TIME;
    }

    static ScoringStrategy forBasketballExtraTime() {
        return BASKETBALLEXTRA_TIME;
    }
}
