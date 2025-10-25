package com.sportradar.exercise.command.modern;

import com.sportradar.exercise.entity.MatchEntity;
import com.sportradar.exercise.service.MatchPersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ModernCommandTest {

    @Mock
    private MatchPersistenceService persistenceService;

    private CommandExecutor commandExecutor;

    @Before
    public void setUp() {
        commandExecutor = new CommandExecutor();
    }

    @Test
    public void testCreateMatchCommand() {
        MatchEntity entity = new MatchEntity("Team A", "Team B", "FOOTBALL");
        entity.setId(1L);
        when(persistenceService.saveMatch(any(MatchEntity.class))).thenReturn(entity);

        CreateMatchCommand command = new CreateMatchCommand(persistenceService, "Team A", "Team B", "FOOTBALL");
        MatchEntity result = commandExecutor.executeCommand(command);

        assertNotNull(result);
        assertEquals("Team A", result.getHomeTeamName());
        assertEquals(1, commandExecutor.getHistorySize());
    }

    @Test
    public void testUpdateScoreCommand() {
        MatchEntity entity = new MatchEntity("Team A", "Team B", "FOOTBALL");
        entity.setId(1L);
        entity.setHomeScore(0);
        entity.setAwayScore(0);
        
        when(persistenceService.findById(1L)).thenReturn(Optional.of(entity));
        when(persistenceService.updateScore(1L, 2, 1)).thenReturn(entity);

        UpdateScoreCommand command = new UpdateScoreCommand(persistenceService, 1L, 2, 1);
        MatchEntity result = commandExecutor.executeCommand(command);

        assertNotNull(result);
        verify(persistenceService).updateScore(1L, 2, 1);
    }

    @Test
    public void testUndoCommand() {
        MatchEntity entity = new MatchEntity("Team A", "Team B", "FOOTBALL");
        entity.setId(1L);
        when(persistenceService.saveMatch(any(MatchEntity.class))).thenReturn(entity);

        CreateMatchCommand command = new CreateMatchCommand(persistenceService, "Team A", "Team B", "FOOTBALL");
        commandExecutor.executeCommand(command);
        
        assertEquals(1, commandExecutor.getHistorySize());
        
        commandExecutor.undoLastCommand();
        
        assertEquals(0, commandExecutor.getHistorySize());
        verify(persistenceService).deleteMatch(1L);
    }
}