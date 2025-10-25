package com.sportradar.exercise;

import com.sportradar.exercise.abstract_factory.*;
import com.sportradar.exercise.match.*;
import com.sportradar.exercise.observer.MatchObserver;
import com.sportradar.exercise.scoring.Scoreboard;
import com.sportradar.exercise.strategy.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntegrationTest {

    @Test
    public void testCompleteMatchLifecycle() {
        // Setup
        Scoreboard scoreboard = Scoreboard.getInstance(new FootballMatchFactory());
        scoreboard.clear(); // Clear any existing state
        
        Team<?> homeTeam = new FootballTeam.Builder().name("Barcelona").build();
        Team<?> awayTeam = new FootballTeam.Builder().name("Madrid").build();
        
        // Create and start match
        FootballMatch match = (FootballMatch) new FootballMatchFactory()
            .createMatchBuilder(homeTeam, awayTeam)
            .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
            .build();
        
        MatchObserver observer = new MatchObserver();
        match.registerObserver(observer);
        
        scoreboard.addMatch(match);
        match.startMatch();
        
        // Update scores
        scoreboard.updateScore(match, 2, 1);
        
        // Verify state
        assertEquals(2, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
        assertTrue(observer.isEventReceived());
        
        // Finish match
        scoreboard.finishMatch(match);
        assertEquals(0, scoreboard.getSummary().size());
        
        scoreboard.clear(); // Reset singleton state
    }

    @Test
    public void testMultiSportTournament() {
        Scoreboard scoreboard = Scoreboard.getInstance(new FootballMatchFactory());
        scoreboard.clear(); // Clear any existing state
        
        // Football match
        MatchInterface footballMatch = new FootballMatchFactory()
            .createMatchBuilder(
                new FootballTeam.Builder().name("Team1").build(),
                new FootballTeam.Builder().name("Team2").build())
            .build();
        
        // Basketball match  
        MatchInterface basketballMatch = new BasketballMatchFactory()
            .createMatchBuilder(
                new BasketballTeam.Builder().name("Team3").build(),
                new BasketballTeam.Builder().name("Team4").build())
            .build();
        
        scoreboard.addMatch(footballMatch);
        scoreboard.addMatch(basketballMatch);
        
        assertEquals(2, scoreboard.getSummary().size());
        
        // Start matches before finishing them
        footballMatch.startMatch();
        basketballMatch.startMatch();
        
        scoreboard.finishAllMatches();
        assertEquals(0, scoreboard.getSummary().size());
        
        scoreboard.clear(); // Reset singleton state
    }
}