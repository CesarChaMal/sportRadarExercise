package com.sportradar.exercise.analytics;

import com.sportradar.exercise.match.MatchInterface;
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
        MatchInterface match1 = Mockito.mock(MatchInterface.class);
        MatchInterface match2 = Mockito.mock(MatchInterface.class);
        MatchInterface match3 = Mockito.mock(MatchInterface.class);

        when(match1.getTotalScore()).thenReturn(5);
        when(match1.getCreationTime()).thenReturn(1000L);

        when(match2.getTotalScore()).thenReturn(10);
        when(match2.getCreationTime()).thenReturn(2000L);

        when(match3.getTotalScore()).thenReturn(5);
        when(match3.getCreationTime()).thenReturn(1500L);

        List<MatchInterface> matches = Arrays.asList(match1, match2, match3);
        List<MatchInterface> sortedMatches = summaryGenerator.generateSummary(matches);

        assertEquals("First match should be the one with the highest total score", match2, sortedMatches.get(0));
        assertEquals("Second match should be newer among those with the same score", match3, sortedMatches.get(1));
        assertEquals("Last match should be the oldest with the lowest score", match1, sortedMatches.get(2));
    }
}
