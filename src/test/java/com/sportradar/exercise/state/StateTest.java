package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StateTest {

    private Match match;

    @Before
    public void setUp() {
        match = new Match.Builder("Team A", "Team B")
                .state(new NotStartedState())
                .build();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFinishedStateBlocksScoreUpdate() {
        match.setState(new FinishedState());
        match.updateScore(1, 0);
    }

    @Test
    public void testInProgressStateAllowsScoreUpdate() {
        match.setState(new InProgressState());
        match.updateScore(1, 0);
        assertEquals(1, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotStartedStateBlocksScoreUpdate() {
        match.setState(new NotStartedState());
        match.updateScore(1, 0);
        assertEquals("Home score should be 1", 1, match.getHomeScore());
        assertEquals("Away score should be 0", 0, match.getAwayScore());
    }

    @Test
    public void testInProgressStateFinishMatch() {
        match.setState(new InProgressState());
        match.setState(new FinishedState());
        assertTrue(match.getState() instanceof FinishedState);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFinishedStateBlocksFinishingAgain() {
        match.setState(new FinishedState());
        match.setState(new FinishedState());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotStartedStateBlocksFinishingMatch() {
        match.setState(new NotStartedState());
        match.setState(new FinishedState());
    }
}