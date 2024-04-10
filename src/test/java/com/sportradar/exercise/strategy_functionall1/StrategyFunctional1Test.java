package com.sportradar.exercise.strategy_functionall1;

import com.sportradar.exercise.abstract_factory.BasketballMatchFactory;
import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import org.junit.Test;

import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;

public class StrategyFunctional1Test {

    private Match createMatchWithStrategy(MatchFactory factory, BiConsumer<Match, int[]> strategy) {
        return factory.createMatchBuilder("Team A", "Team B")
                .scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL1)
                .state(MatchState.forInProgressState())
                .scoringStrategyFunctional1(strategy)
                .build();
    }

    @Test
    public void testFootballNormalTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new FootballMatchFactory(), ScoringStrategiesFunctional1.forFootballNormalTimeScoringStrategy);
        match.updateScore(3, 3);
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testFootballExtraTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new FootballMatchFactory(), ScoringStrategiesFunctional1.forFootballExtraTimeScoringStrategy);
        match.updateScore(2, 2);
        ScoringStrategiesFunctional1.forFootballExtraTimeScoringStrategy.accept(match, new int[]{1, 1});
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testBasketballExtraTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new BasketballMatchFactory(), ScoringStrategiesFunctional1.forBasketballExtraTimeScoringStrategy);
        match.updateScore(20, 20);
        ScoringStrategiesFunctional1.forBasketballExtraTimeScoringStrategy.accept(match, new int[]{10, 5});
        assertEquals("Home score should be 30", 30, match.getHomeScore());
        assertEquals("Away score should be 25", 25, match.getAwayScore());
    }

    @Test
    public void testBasketballNormalTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new BasketballMatchFactory(), ScoringStrategiesFunctional1.forBasketballNormalTimeScoringStrategy);
        match.updateScore(10, 5);
        assertEquals("Home score should be 10", 10, match.getHomeScore());
        assertEquals("Away score should be 5", 5, match.getAwayScore());
    }
}
