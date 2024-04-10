package com.sportradar.exercise.match;

import com.sportradar.exercise.state.NotStartedState;
import com.sportradar.exercise.strategy.ScoringStrategy;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import com.sportradar.exercise.strategy_functionall1.ScoringStrategiesFunctional1;
import org.junit.Before;
import org.junit.Test;

import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BuilderTest {

    private Match.Builder builder;

    @Before
    public void setUp() {
        builder = new Match.Builder("Team A", "Team B")
                .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
                .scoringStrategy(ScoringStrategy.forFootballNormalTime());
    }

    @Test
    public void testBuilderInitializesTeamsCorrectly() {
        Match match = builder.build();
        assertEquals("Team A should be set as home team", "Team A", match.getHomeTeam());
        assertEquals("Team B should be set as away team", "Team B", match.getAwayTeam());
    }

    @Test
    public void testBuilderInitializesScoresToZero() {
        Match match = builder.build();
        assertEquals("Initial home score should be 0", 0, match.getHomeScore());
        assertEquals("Initial away score should be 0", 0, match.getAwayScore());
    }

    @Test
    public void testBuilderAppliesCustomScore() {
        Match match = builder.homeScore(1).awayScore(2).build();
        assertEquals("Home score should be set to 1", 1, match.getHomeScore());
        assertEquals("Away score should be set to 2", 2, match.getAwayScore());
    }

    @Test
    public void testBuilderSetsNotStartedStateByDefault() {
        Match match = builder.build();
        assertTrue("Match state should be NotStartedState by default", match.getState() instanceof NotStartedState);
    }

    @Test
    public void testBuilderAppliesCustomScoringStrategy() {
        ScoringStrategy customStrategy = ScoringStrategy.forFootballNormalTime();
        Match match = builder.scoringStrategy(customStrategy).build();
        assertEquals("Custom scoring strategy should be applied", customStrategy, match.getScoringStrategy());
    }
}
