package com.sportradar.exercise.service;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.match.FootballMatch;
import com.sportradar.exercise.match.FootballTeam;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.match.Team;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ScoreboardServiceTest {

    private ScoreboardService scoreboardService;
    private Team<?> homeTeam;
    private Team<?> awayTeam;

    @Before
    public void setUp() {
        scoreboardService = new ScoreboardService(new FootballMatchFactory());
        homeTeam = new FootballTeam.Builder().name("Home Team").build();
        awayTeam = new FootballTeam.Builder().name("Away Team").build();
    }

    @Test
    public void testStartMatchAsync() throws Exception {
        boolean result = scoreboardService.startMatchAsync(homeTeam, awayTeam).get();
        assertTrue(result);
        assertEquals(1, scoreboardService.getAllMatches().size());
    }

    @Test
    public void testUpdateScore() {
        MatchInterface match = new FootballMatchFactory().createMatchBuilder(homeTeam, awayTeam).build();
        scoreboardService.addMatch(match);
        
        scoreboardService.updateScore(match, 2, 1);
        
        assertEquals(2, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
    }

    @Test
    public void testFinishMatch() {
        MatchInterface match = new FootballMatchFactory().createMatchBuilder(homeTeam, awayTeam).build();
        scoreboardService.addMatch(match);
        
        scoreboardService.finishMatch(match);
        
        assertEquals(0, scoreboardService.getAllMatches().size());
    }

    @Test
    public void testGetSummary() {
        MatchInterface match1 = new FootballMatchFactory().createMatchBuilder(homeTeam, awayTeam).build();
        match1.setHomeScore(2);
        match1.setAwayScore(1);
        scoreboardService.addMatch(match1);
        
        List<MatchInterface> summary = scoreboardService.getSummary();
        
        assertFalse(summary.isEmpty());
        assertEquals(1, summary.size());
    }

    @Test
    public void testClear() {
        MatchInterface match = new FootballMatchFactory().createMatchBuilder(homeTeam, awayTeam).build();
        scoreboardService.addMatch(match);
        
        scoreboardService.clear();
        
        assertTrue(scoreboardService.getAllMatches().isEmpty());
    }
}