package com.sportradar.exercise.service;

import com.sportradar.exercise.entity.MatchEntity;
import com.sportradar.exercise.exception.MatchNotFoundException;
import com.sportradar.exercise.repository.MatchRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MatchPersistenceServiceTest {

    @Mock
    private MatchRepository matchRepository;

    private MatchPersistenceService persistenceService;

    @Before
    public void setUp() {
        persistenceService = new MatchPersistenceService(matchRepository);
    }

    @Test
    public void testSaveMatch() {
        MatchEntity entity = new MatchEntity("Team A", "Team B", "FOOTBALL");
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(entity);

        MatchEntity saved = persistenceService.saveMatch(entity);

        assertNotNull(saved);
        verify(matchRepository).save(entity);
    }

    @Test
    public void testFindById() {
        MatchEntity entity = new MatchEntity("Team A", "Team B", "FOOTBALL");
        when(matchRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<MatchEntity> found = persistenceService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("Team A", found.get().getHomeTeamName());
    }

    @Test
    public void testUpdateScore() {
        MatchEntity entity = new MatchEntity("Team A", "Team B", "FOOTBALL");
        entity.setId(1L);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(entity);

        MatchEntity updated = persistenceService.updateScore(1L, 2, 1);

        assertEquals(Integer.valueOf(2), updated.getHomeScore());
        assertEquals(Integer.valueOf(1), updated.getAwayScore());
    }

    @Test(expected = MatchNotFoundException.class)
    public void testUpdateScoreNotFound() {
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());

        persistenceService.updateScore(1L, 2, 1);
    }

    @Test
    public void testFindActiveMatches() {
        List<MatchEntity> matches = Arrays.asList(new MatchEntity("Team A", "Team B", "FOOTBALL"));
        when(matchRepository.findActiveMatchesSorted()).thenReturn(matches);

        List<MatchEntity> result = persistenceService.findActiveMatches();

        assertEquals(1, result.size());
        verify(matchRepository).findActiveMatchesSorted();
    }
}