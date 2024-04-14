package com.sportradar.exercise.command;

import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.EventType;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.scoring.Scoreboard;
import com.sportradar.exercise.state.FinishedState;
import com.sportradar.exercise.state.InProgressState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommandMockingTest {
    private Scoreboard scoreboard;
    private MatchFactory matchFactory;
    private MatchInterface match;
    private Team<?> homeTeam;
    private Team<?> awayTeam;

    @Before
    public void setUp() {
        scoreboard = Mockito.mock(Scoreboard.class);
        matchFactory = Mockito.mock(MatchFactory.class);
        match = Mockito.mock(Match.class);
        homeTeam = Mockito.mock(Team.class);
        awayTeam = Mockito.mock(Team.class);

        Match.Builder builderMock = Mockito.mock(Match.Builder.class);

        when(builderMock.build()).thenReturn((Match) match);
        when(matchFactory.createMatchBuilder(any(Team.class), any(Team.class))).thenReturn(builderMock);
    }

    @Test
    public void testStartMatchCommand() {
        StartMatchCommand startMatchCommand = new StartMatchCommand(scoreboard, matchFactory, homeTeam, awayTeam);
        startMatchCommand.execute();
        verify(scoreboard, times(1)).addMatch(any(MatchInterface.class));
    }

    @Test
    public void testUpdateScoreCommand_AllowsUpdate() {
        when(match.getHomeScore()).thenReturn(1);
        when(match.getAwayScore()).thenReturn(1);
        when(match.getState()).thenReturn(new InProgressState());
//        doNothing().when(match).updateScore(anyInt(), anyInt());
        doNothing().when(match).updateScore(eq(EventType.SCORE_UPDATE), anyInt(), anyInt());

        UpdateScoreCommand updateScoreCommand = new UpdateScoreCommand(match, 1, 1);
        updateScoreCommand.execute();

        verify(match, times(1)).updateScore(EventType.SCORE_UPDATE, 1, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateScoreCommand_BlocksUpdate() {
        when(match.getState()).thenReturn(new FinishedState());
        UpdateScoreCommand updateScoreCommand = new UpdateScoreCommand(match, 1, 1);
        updateScoreCommand.execute();
    }

    @Test
    public void testFinishMatchCommand() {
        FinishMatchCommand finishMatchCommand = new FinishMatchCommand(scoreboard, match);
        finishMatchCommand.execute();
        verify(scoreboard, times(1)).removeMatch(match);
    }
}
