package com.sportradar.exercise.command;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.*;
import com.sportradar.exercise.scoring.Scoreboard;
import com.sportradar.exercise.state.MatchState;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class CommandTest {
    private Scoreboard scoreboard;
    private MatchFactory<FootballMatch> matchFactory;
    private FootballMatch match;
    private Team<FootballPlayer> homeTeam;
    private Team<FootballPlayer> awayTeam;

    @Before
    public void setUp() {
        matchFactory = new FootballMatchFactory();
//        scoreboard = new Scoreboard(matchFactory);
        scoreboard = Scoreboard.getInstance(matchFactory);
        homeTeam = new FootballTeam.Builder().name("Home Team").build();
        awayTeam = new FootballTeam.Builder().name("Away Team").build();

        match = (FootballMatch) matchFactory.createMatchBuilder(homeTeam, awayTeam)
                            .build();
    }

    @Test
    public void testStartMatchCommand() {
        StartMatchCommand startMatchCommand = new StartMatchCommand(scoreboard, matchFactory, homeTeam, awayTeam);
        startMatchCommand.execute();

        Optional<MatchInterface> updatedMatchOpt = scoreboard.getMatch(match.getHomeTeam(), match.getAwayTeam());
        assertTrue("Match should exist", updatedMatchOpt.isPresent());
        MatchInterface updatedMatch = updatedMatchOpt.get();

        assertFalse("Scoreboard should contain the match", scoreboard.getAllMatches().isEmpty());
        assertTrue("Match should be in progress", updatedMatch.getState() == MatchState.IN_PROGRESS);
    }

    @Test
    public void testUpdateScoreCommand_AllowsUpdate() {
        match.setState(MatchState.IN_PROGRESS);
        match.setHomeScore(0);
        match.setAwayScore(0);

        UpdateScoreCommand updateScoreCommand = new UpdateScoreCommand(match, 1, 1);
        updateScoreCommand.execute();
        assertEquals("Home score should be updated", 1, match.getHomeScore());
        assertEquals("Away score should be updated", 1, match.getAwayScore());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpdateScoreCommand_BlocksUpdate() {
        match.setState(MatchState.FINISHED);
        UpdateScoreCommand updateScoreCommand = new UpdateScoreCommand(match, 1, 1);
        updateScoreCommand.execute();
    }

    @Test
    public void testFinishMatchCommand() {
        match.setState(MatchState.IN_PROGRESS);
        scoreboard.addMatch(match);

        FinishMatchCommand finishMatchCommand = new FinishMatchCommand(scoreboard, match);
        finishMatchCommand.execute();
        assertTrue("Match should be finished", match.getState() == MatchState.FINISHED);
        assertTrue("Scoreboard should no longer contain the match", scoreboard.getAllMatches().isEmpty());
    }
}
