package com.sportradar.exercise.match;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuilderTest {

    private Match.Builder builder;

    @Before
    public void setUp() {
        builder = new Match.Builder("Team A", "Team B");
    }

    @Test
    public void testBuilderInitializesTeamsCorrectly() {
        Match match = builder.build();
        assertEquals("Team A should be set as home team", "Team A", match.getHomeTeam());
        assertEquals("Team B should be set as away team", "Team B", match.getAwayTeam());
    }
}
