package com.sportradar.exercise.storage;

import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.storage.InMemoryMatchStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.*;

public class InMemoryMatchStorageTest {
    private InMemoryMatchStorage storage;
    private MatchInterface match1;
    private MatchInterface match2;

    @Before
    public void setUp() {
        storage = new InMemoryMatchStorage();
        match1 = Mockito.mock(MatchInterface.class);
        match2 = Mockito.mock(MatchInterface.class);
        Mockito.when(match1.getId()).thenReturn(1L);
        Mockito.when(match2.getId()).thenReturn(2L);

        storage.addMatch(match1);
        storage.addMatch(match2);
    }

    @Test
    public void testAddMatch() {
        MatchInterface newMatch = Mockito.mock(MatchInterface.class);
        Mockito.when(newMatch.getId()).thenReturn(3L);
        storage.addMatch(newMatch);
        assertNotNull("Match should be added", storage.getMatchById(3L));
    }

    @Test
    public void testRemoveMatch() {
        storage.removeMatch(match1);
        assertNull("Match should be removed", storage.getMatchById(1L));
    }

    @Test
    public void testGetMatchById() {
        MatchInterface retrievedMatch = storage.getMatchById(1L);
        assertEquals("Retrieved match should be match1", match1, retrievedMatch);
    }

    @Test
    public void testGetAllMatches() {
        List<MatchInterface> matches = storage.getAllMatches();
        assertEquals("Should return all matches", 2, matches.size());
        assertTrue("Should contain match1", matches.contains(match1));
        assertTrue("Should contain match2", matches.contains(match2));
    }
}
