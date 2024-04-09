package com.sportradar.exercise.strategy;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.state.InProgressState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrategyTest {

    private Match match;

    @Before
    public void setUp() {
        match = new Match.Builder("Team A", "Team B")
                .state(new InProgressState())
                .scoringStrategy(ScoringStrategy.forNormalTime())
                .build();
    }

    @Test
    public void testNormalTimeScoringStrategy() {
        ScoringStrategy normalTimeStrategy = ScoringStrategy.forNormalTime();
        normalTimeStrategy.calculateScore(match, 3, 3);
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testExtraTimeScoringStrategy() {
        match.updateScore(2, 2);
        ScoringStrategy extraTimeStrategy = ScoringStrategy.forExtraTime();
        extraTimeStrategy.calculateScore(match, 1, 1);
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }
}
