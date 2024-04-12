package com.sportradar.exercise.strategy_functional2;

import com.sportradar.exercise.abstract_factory.BasketballMatchFactory;
import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.*;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategiesFunctional2;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategyType;
import org.junit.Test;

import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;

public class StrategyFunctional2Test {

    private Match createMatchWithStrategy(MatchFactory factory, Team<?> teamA, Team<?> teamB, ScoringStrategyType strategyType) {
        Match match = factory.createMatchBuilder(teamA, teamB)
                .scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL2)
                .state(MatchState.forInProgressState())
                .build();
        match.setScoringStrategyFunctional2(strategyType);
        return match;
    }

    @Test
    public void testFootballNormalTimeScoringStrategy() {
        Team<?> teamA = new FootballTeam.Builder().name("Team A").build();
        Team<?> teamB = new FootballTeam.Builder().name("Team B").build();

        Match match = createMatchWithStrategy(new FootballMatchFactory(), teamA, teamB, ScoringStrategyType.FOOTBALL_NORMAL_TIME);
        match.updateScore(3, 3);
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testFootballExtraTimeScoringStrategy() {
        Team<?> teamA = new FootballTeam.Builder().name("Team A").build();
        Team<?> teamB = new FootballTeam.Builder().name("Team B").build();

        Match match = createMatchWithStrategy(new FootballMatchFactory(), teamA, teamB, ScoringStrategyType.FOOTBALL_EXTRA_TIME);

        BiConsumer<Match, int[]> strategy = ScoringStrategiesFunctional2.getStrategy(ScoringStrategyType.FOOTBALL_EXTRA_TIME);
        match.updateScore(2, 2);
        strategy.accept(match, new int[]{1, 1});
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testBasketballExtraTimeScoringStrategy() {
        Team<?> teamA = new BasketballTeam.Builder().name("Team A").build();
        Team<?> teamB = new BasketballTeam.Builder().name("Team B").build();

        Match match = createMatchWithStrategy(new BasketballMatchFactory(), teamA, teamB, ScoringStrategyType.BASKETBALL_EXTRA_TIME);

        BiConsumer<Match, int[]> strategy = ScoringStrategiesFunctional2.getStrategy(ScoringStrategyType.BASKETBALL_EXTRA_TIME);
        match.updateScore(20, 20);
        strategy.accept(match, new int[]{10, 5});
        assertEquals("Home score should be 30", 30, match.getHomeScore());
        assertEquals("Away score should be 25", 25, match.getAwayScore());
    }

    @Test
    public void testBasketballNormalTimeScoringStrategy() {
        Team<?> teamA = new BasketballTeam.Builder().name("Team A").build();
        Team<?> teamB = new BasketballTeam.Builder().name("Team B").build();

        Match match = createMatchWithStrategy(new BasketballMatchFactory(), teamA, teamB, ScoringStrategyType.BASKETBALL_NORMAL_TIME);

        match.updateScore(10, 5);
        assertEquals("Home score should be 10", 10, match.getHomeScore());
        assertEquals("Away score should be 5", 5, match.getAwayScore());
    }
}
