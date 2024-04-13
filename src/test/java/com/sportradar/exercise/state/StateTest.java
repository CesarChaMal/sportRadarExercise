package com.sportradar.exercise.state;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.FootballTeam;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.strategy.ScoringStrategy;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import com.sportradar.exercise.strategy_functionall1.ScoringStrategiesFunctional1;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StateTest {

    private Match match;
    private MatchFactory matchFactory;

    @Before
    public void setUp() {
        matchFactory = new FootballMatchFactory();

        Team<?> teamA = FootballTeam.builder().name("Team A").build();
        Team<?> teamB = FootballTeam.builder().name("Team B").build();

        match = matchFactory.createMatchBuilder(teamA, teamB)
                .state(MatchState.forNotStartedState())
                .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
                .scoringStrategy(ScoringStrategy.forFootballNormalTime())
                .build();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFinishedStateBlocksScoreUpdate() {
//        match.setState(MatchState.forFinishedState());
        match.finishMatch();
        match.updateScore(1, 0);
    }

    @Test
        public void testInProgressStateAllowsScoreUpdate() {
//        match.setState(MatchState.forInProgressState());
        match.startMatch();
        match.updateScore(1, 0);
        assertEquals("Home score should be updated", 1, match.getHomeScore());
        assertEquals("Away score should remain unchanged", 0, match.getAwayScore());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotStartedStateBlocksScoreUpdate() {
        match.updateScore(1, 0);
        assertEquals("Home score should be 1", 1, match.getHomeScore());
        assertEquals("Away score should be 0", 0, match.getAwayScore());
    }

    @Test
    public void testInProgressStateFinishMatch() {
//        one way to do it
//        match.setState(MatchState.forInProgressState());
//        match.setState(MatchState.forFinishedState());

        match.startMatch();
        match.finishMatch();
        assertTrue("Match should be in FinishedState", match.getState() instanceof FinishedState);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFinishedStateBlocksFinishingAgain() {
//        match.setState(MatchState.forFinishedState());
//        match.setState(MatchState.forFinishedState());
        match.finishMatch();
        match.finishMatch();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotStartedStateBlocksFinishingMatch() {
//        match.setState(MatchState.forFinishedState());
        match.finishMatch();
    }
}
