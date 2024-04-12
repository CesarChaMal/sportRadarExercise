package com.sportradar.exercise.abstract_factory;

import com.sportradar.exercise.match.*;
import com.sportradar.exercise.state.MatchState;

public class BasketballMatchFactory implements MatchFactory<BasketballMatch> {
    @Override
    public BasketballMatch.Builder createMatchBuilder(Team<?> homeTeam, Team<?> awayTeam) {
        return (BasketballMatch.Builder) new BasketballMatch.Builder(homeTeam, awayTeam)
                // Uncomment and configure scoring strategies as needed
//                .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
//                .scoringStrategy(ScoringStrategy.forBasketballNormalTime())
//                .scoringStrategyFunctional1(ScoringStrategiesFunctional1.forBasketballNormalTimeScoringStrategy)
//                .scoringStrategyFunctional2(ScoringStrategyType.BASKETBALL_NORMAL_TIME)
                .eventManagerFactory(BasketballEventManager::new)
                .state(MatchState.forNotStartedState()); // Set the initial state of the match
    }
}
