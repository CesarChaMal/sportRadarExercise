package com.sportradar.exercise.command;

import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.scoring.Scoreboard;
import com.sportradar.exercise.state.FinishedState;
import com.sportradar.exercise.state.InProgressState;
import com.sportradar.exercise.state.MatchState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class CommandTest {
    private Scoreboard scoreboard;
    private MatchFactory matchFactory;
    private MatchInterface match;

    @Before
    public void setUp() {
        scoreboard = Mockito.mock(Scoreboard.class);
        matchFactory = Mockito.mock(MatchFactory.class);
        match = Mockito.mock(Match.class);

        Match.Builder builderMock = Mockito.mock(Match.Builder.class);

        when(builderMock.build()).thenReturn((Match) match);
        when(matchFactory.createMatchBuilder(anyString(), anyString())).thenReturn(builderMock);
    }

    @Test
    public void testStartMatchCommand() {
        StartMatchCommand startMatchCommand = new StartMatchCommand(scoreboard, matchFactory, "HomeTeam", "AwayTeam");
        startMatchCommand.execute();
        verify(scoreboard, times(1)).addMatch(any(MatchInterface.class));
    }

    @Test
    public void testUpdateScoreCommand_AllowsUpdate() {
        when(match.getState()).thenReturn(MatchState.forInProgressState());
        doNothing().when(match).updateScore(anyInt(), anyInt());

        UpdateScoreCommand updateScoreCommand = new UpdateScoreCommand(match, 1, 1);
        updateScoreCommand.execute();

        verify(match, times(1)).updateScore(1, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateScoreCommand_BlocksUpdate() {
        when(match.getState()).thenReturn(MatchState.forFinishedState());
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
