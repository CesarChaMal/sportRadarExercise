package com.sportradar.exercise;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testSummaryOrderWithMultipleMatches() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");

        Match mexicoVsCanada = scoreboard.getMatch("Mexico", "Canada");
        Match spainVsBrazil = scoreboard.getMatch("Spain", "Brazil");
        Match germanyVsFrance = scoreboard.getMatch("Germany", "France");
        Match uruguayVsItaly = scoreboard.getMatch("Uruguay", "Italy");
        Match argentinaVsAustralia = scoreboard.getMatch("Argentina", "Australia");

        scoreboard.updateScore(mexicoVsCanada, 0, 5);
        scoreboard.updateScore(spainVsBrazil, 10, 2);
        scoreboard.updateScore(germanyVsFrance, 2, 2);
        scoreboard.updateScore(uruguayVsItaly, 6, 6);
        scoreboard.updateScore(argentinaVsAustralia, 3, 1);

        List<Match> summary = scoreboard.getSummary();

        assertEquals("Uruguay vs Italy should be first", "Uruguay", summary.get(0).getHomeTeam());
        assertEquals("Spain vs Brazil should be second", "Spain", summary.get(1).getHomeTeam());
        assertEquals("Mexico vs Canada should be third", "Mexico", summary.get(2).getHomeTeam());
        assertEquals("Argentina vs Australia should be fourth", "Argentina", summary.get(3).getHomeTeam());
        assertEquals("Germany vs France should be fifth", "Germany", summary.get(4).getHomeTeam());
    }
}
