package com.sportradar.exercise.abstract_factory;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.state.NotStartedState;
import com.sportradar.exercise.strategy.ScoringStrategy;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import com.sportradar.exercise.strategy_functionall1.ScoringStrategiesFunctional1;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategiesFunctional2;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategyType;

public class BasketballMatchFactory implements MatchFactory {
    @Override
    public Match.Builder createMatchBuilder(String homeTeam, String awayTeam) {
        return new Match.Builder(homeTeam, awayTeam)
//                .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
//                .scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL1)
//                .scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL2)
//                .scoringStrategy(ScoringStrategy.forBasketballNormalTime())
//                .scoringStrategyFunctional1(ScoringStrategiesFunctional1.forBasketballNormalTimeScoringStrategy)
//                .scoringStrategyFunctional2(ScoringStrategyType.BASKETBALL_NORMAL_TIME);
                .state(MatchState.forNotStartedState());
    }
}