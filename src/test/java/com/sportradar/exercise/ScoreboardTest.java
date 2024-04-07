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

    @Test
    public void testFinishMatch() {
        scoreboard.startMatch("Home3", "Away3");
        Match match = scoreboard.getMatch("Home3", "Away3");
        scoreboard.finishMatch(match);
        assertTrue("Match should be finished and removed", scoreboard.getSummary().isEmpty());
    }

    @Test
    public void testGetSummary() {
        scoreboard.startMatch("Home1", "Away1");
        scoreboard.startMatch("Home2", "Away2");

        Match match1 = scoreboard.getMatch("Home1", "Away1");
        Match match2 = scoreboard.getMatch("Home2", "Away2");

        scoreboard.updateScore(match1, 0, 5);
        scoreboard.updateScore(match2, 10, 2);

        List<Match> summary = scoreboard.getSummary();

        assertTrue("Summary should contain both matches", summary.size() == 2);
        assertEquals("The first match should have the higher total score", 12, summary.get(0).getTotalScore());
        assertEquals("The second match should have the lower total score", 5, summary.get(1).getTotalScore());
    }

}
