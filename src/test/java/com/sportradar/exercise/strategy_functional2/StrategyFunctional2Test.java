package com.sportradar.exercise.strategy_functional2;

import com.sportradar.exercise.abstract_factory.BasketballMatchFactory;
import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategiesFunctional2;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategyType;
import org.junit.Test;

import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;

public class StrategyFunctional2Test {

    private Match createMatchWithStrategy(MatchFactory factory, ScoringStrategyType strategyType) {
        Match match = factory.createMatchBuilder("Team A", "Team B")
                .scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL2)
                .state(MatchState.forInProgressState())
                .build();
        BiConsumer<Match, int[]> strategy = ScoringStrategiesFunctional2.getStrategy(strategyType);
        match.setScoringStrategyFunctional2(strategyType);
        return match;
    }

    @Test
    public void testFootballNormalTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new FootballMatchFactory(), ScoringStrategyType.FOOTBALL_NORMAL_TIME);
        match.updateScore(3, 3);
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testFootballExtraTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new FootballMatchFactory(), ScoringStrategyType.FOOTBALL_EXTRA_TIME);
        BiConsumer<Match, int[]> strategy = ScoringStrategiesFunctional2.getStrategy(ScoringStrategyType.FOOTBALL_EXTRA_TIME);
        match.updateScore(2, 2);
        strategy.accept(match, new int[]{1, 1});
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testBasketballExtraTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new BasketballMatchFactory(), ScoringStrategyType.BASKETBALL_EXTRA_TIME);
        BiConsumer<Match, int[]> strategy = ScoringStrategiesFunctional2.getStrategy(ScoringStrategyType.BASKETBALL_EXTRA_TIME);
        match.updateScore(20, 20);
        strategy.accept(match, new int[]{10, 5});
        assertEquals("Home score should be 30", 30, match.getHomeScore());
        assertEquals("Away score should be 25", 25, match.getAwayScore());
    }

    @Test
    public void testBasketballNormalTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new BasketballMatchFactory(), ScoringStrategyType.BASKETBALL_NORMAL_TIME);
        match.updateScore(10, 5);
        assertEquals("Home score should be 10", 10, match.getHomeScore());
        assertEquals("Away score should be 5", 5, match.getAwayScore());
    }
}
