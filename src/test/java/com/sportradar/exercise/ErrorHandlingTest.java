package com.sportradar.exercise;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.match.*;
import com.sportradar.exercise.scoring.Scoreboard;
import org.junit.Test;

import static org.junit.Assert.*;

public class ErrorHandlingTest {

    @Test(expected = NullPointerException.class)
    public void testNullHomeTeam() {
        new FootballMatchFactory().createMatchBuilder(null, 
            new FootballTeam.Builder().name("Away").build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeScore() {
        Team<?> team = new FootballTeam.Builder().name("Test").build();
        Match match = new FootballMatchFactory().createMatchBuilder(team, team).build();
        match.setHomeScore(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidScoringTeam() {
        Team<?> homeTeam = new FootballTeam.Builder().name("Home").build();
        Team<?> awayTeam = new FootballTeam.Builder().name("Away").build();
        Team<?> invalidTeam = new FootballTeam.Builder().name("Invalid").build();
        
        Match match = new FootballMatchFactory().createMatchBuilder(homeTeam, awayTeam).build();
        match.incrementScore(EventType.GOAL, invalidTeam, 1);
    }

    @Test
    public void testScoreboardResourceCleanup() {
        Scoreboard scoreboard = Scoreboard.getInstance(new FootballMatchFactory());
        scoreboard.shutdown();
        // Verify no exceptions during cleanup
        assertTrue("Cleanup should complete without errors", true);
    }
}