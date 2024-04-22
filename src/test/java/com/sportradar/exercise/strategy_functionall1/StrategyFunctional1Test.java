package com.sportradar.exercise.strategy_functionall1;

import com.sportradar.exercise.abstract_factory.BasketballMatchFactory;
import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.*;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import org.junit.Test;

import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;

public class StrategyFunctional1Test {

    private Match createMatchWithStrategy(MatchFactory<? extends Match> factory, Team<?> teamA, Team<?> teamB, BiConsumer<Match, int[]> strategy) {
        return factory.createMatchBuilder(teamA, teamB)
                .scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL1)
                .state(MatchState.forInProgressState())
                .scoringStrategyFunctional1(strategy)
                .build();
    }

    @Test
    public void testFootballNormalTimeScoringStrategy() {
        Team<?> teamA = new FootballTeam.Builder().name("Team A").build();
        Team<?> teamB = new FootballTeam.Builder().name("Team B").build();
        FootballMatch match = (FootballMatch) createMatchWithStrategy(new FootballMatchFactory(), teamA, teamB, ScoringStrategiesFunctional1.forFootballNormalTimeScoringStrategy);
        match.updateScore(EventType.SCORE_UPDATE, 3, 3);
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testFootballExtraTimeScoringStrategy() {
        Team<?> teamA = new FootballTeam.Builder().name("Team A").build();
        Team<?> teamB = new FootballTeam.Builder().name("Team B").build();
        FootballMatch match = (FootballMatch) createMatchWithStrategy(new FootballMatchFactory(), teamA, teamB, ScoringStrategiesFunctional1.forFootballExtraTimeScoringStrategy);
        match.updateScore(EventType.SCORE_UPDATE, 2, 2);
        ScoringStrategiesFunctional1.forFootballExtraTimeScoringStrategy.accept(match, new int[]{1, 1});
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testBasketballNormalTimeScoringStrategy() {
        Team<?> teamA = new BasketballTeam.Builder().name("Team A").build();
        Team<?> teamB = new BasketballTeam.Builder().name("Team B").build();
        BasketballMatch match = (BasketballMatch) createMatchWithStrategy(new BasketballMatchFactory(), teamA, teamB, ScoringStrategiesFunctional1.forBasketballNormalTimeScoringStrategy);
        match.updateScore(EventType.SCORE_UPDATE, 10, 5);
        assertEquals("Home score should be 10", 10, match.getHomeScore());
        assertEquals("Away score should be 5", 5, match.getAwayScore());
    }

    @Test
    public void testBasketballExtraTimeScoringStrategy() {
        Team<?> teamA = new BasketballTeam.Builder().name("Team A").build();
        Team<?> teamB = new BasketballTeam.Builder().name("Team B").build();
        BasketballMatch match = (BasketballMatch) createMatchWithStrategy(new BasketballMatchFactory(), teamA, teamB, ScoringStrategiesFunctional1.forBasketballExtraTimeScoringStrategy);
        match.updateScore(EventType.SCORE_UPDATE, 20, 20);
        ScoringStrategiesFunctional1.forBasketballExtraTimeScoringStrategy.accept(match, new int[]{10, 5});
        assertEquals("Home score should be 30", 30, match.getHomeScore());
        assertEquals("Away score should be 25", 25, match.getAwayScore());
    }
}
