package com.sportradar.exercise.state;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.match.*;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import com.sportradar.exercise.state.FinishedState;

public class MatchStateManagerTest {
    private Match match;
    private MatchStateManager stateManager;

    @Before
    public void setUp() {
        Team<?> homeTeam = new FootballTeam.Builder().name("Home").build();
        Team<?> awayTeam = new FootballTeam.Builder().name("Away").build();
        
        match = new FootballMatchFactory().createMatchBuilder(homeTeam, awayTeam)
                .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
                .build();
        stateManager = match.getStateManager();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidStateTransition() {
        stateManager.transitionState(MatchState.forFinishedState());
    }

    @Test
    public void testValidStateTransitions() {
        stateManager.transitionState(MatchState.forInProgressState());
        assertEquals(MatchState.forInProgressState().getClass(), 
                    stateManager.getCurrentState().getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testScoreUpdateInInvalidState() {
        stateManager.handleScoreUpdate(1, 0);
    }

    @Test
    public void testConcurrentStateTransitions() throws InterruptedException {
        Thread t1 = new Thread(() -> stateManager.transitionState(MatchState.forInProgressState()));
        Thread t2 = new Thread(() -> {
            try { Thread.sleep(10); } catch (InterruptedException e) {}
            stateManager.transitionState(MatchState.forFinishedState());
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        assertTrue(stateManager.getCurrentState() instanceof FinishedState);
    }
}