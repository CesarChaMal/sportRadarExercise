package com.sportradar.exercise.analytics;

import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.analytics.MatchSummaryGenerator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MatchSummaryGeneratorTest {

    private MatchSummaryGenerator summaryGenerator;

    @Before
    public void setUp() {
        summaryGenerator = new MatchSummaryGenerator();
    }

    @Test
    public void generateSummary_SortsMatchesCorrectly() {
        // Create mock MatchInterface objects with Mockito
        MatchInterface match1 = Mockito.mock(MatchInterface.class);
        MatchInterface match2 = Mockito.mock(MatchInterface.class);
        MatchInterface match3 = Mockito.mock(MatchInterface.class);

        // Setup mocks with expected behavior
        // Assuming getCreationTime returns a timestamp where a higher value means a newer match
        when(match1.getTotalScore()).thenReturn(5);
        when(match1.getCreationTime()).thenReturn(1000L);

        when(match2.getTotalScore()).thenReturn(10);
        when(match2.getCreationTime()).thenReturn(2000L);

        when(match3.getTotalScore()).thenReturn(5);
        when(match3.getCreationTime()).thenReturn(1500L);

        List<MatchInterface> matches = Arrays.asList(match1, match2, match3);
        List<MatchInterface> sortedMatches = summaryGenerator.generateSummary(matches);

        // Verify that match2 is first because it has the highest score,
        // then match3 because it has the same score as match1 but is newer,
        // and match1 is last.
        assertEquals("First match should be the one with the highest total score", match2, sortedMatches.get(0));
        assertEquals("Second match should be newer among those with the same score", match3, sortedMatches.get(1));
        assertEquals("Last match should be the oldest with the lowest score", match1, sortedMatches.get(2));
    }
}
