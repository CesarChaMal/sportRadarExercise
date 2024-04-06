package com.sportradar.exercise;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ScoreboardTest {
    private Scoreboard scoreboard;

    @Before
    public void setUp() {
        scoreboard = new Scoreboard();
    }

    @Test
    public void testStartMatch() {
        scoreboard.startMatch("Home Team", "Away Team");
        var summary = scoreboard.getSummary();
        assertEquals(1, summary.size());

        var match = summary.get(0);
        assertEquals("Home Team", match.getHomeTeam());
        assertEquals("Away Team", match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }
}
