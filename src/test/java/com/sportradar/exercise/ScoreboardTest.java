package com.sportradar.exercise;

import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static java.lang.System.out;
import static org.junit.Assert.*;

public class ScoreboardTest {
    private Scoreboard scoreboard;

    @Before
    public void setUp() {
        scoreboard = new Scoreboard();
    }

    @Test
    public void testStartMatch() {
        scoreboard.startMatch("Home1", "Away1");
        var summary = scoreboard.getSummary();
        assertEquals(1, summary.size());

        var match = summary.get(0);
        assertEquals("Home1", match.getHomeTeam());
        assertEquals("Away1", match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    public void testUpdateScore() {
        scoreboard.startMatch("Home2", "Away2");
        Match match = scoreboard.getMatch("Home2", "Away2");
        scoreboard.updateScore(match, 2, 2);
        assertEquals("Home score not updated as expected", 2, match.getHomeScore());
        assertEquals("Away score not updated as expected", 2, match.getAwayScore());
    }
}
