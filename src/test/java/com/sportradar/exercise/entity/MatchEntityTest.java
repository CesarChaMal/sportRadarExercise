package com.sportradar.exercise.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class MatchEntityTest {

    @Test
    public void testMatchEntityCreation() {
        MatchEntity entity = new MatchEntity("Team A", "Team B", "FOOTBALL");
        
        assertEquals("Team A", entity.getHomeTeamName());
        assertEquals("Team B", entity.getAwayTeamName());
        assertEquals("FOOTBALL", entity.getMatchType());
        assertEquals(Integer.valueOf(0), entity.getHomeScore());
        assertEquals(Integer.valueOf(0), entity.getAwayScore());
        assertEquals("NOT_STARTED", entity.getStatus());
        assertNotNull(entity.getStartTime());
    }

    @Test
    public void testScoreUpdate() {
        MatchEntity entity = new MatchEntity("Team A", "Team B", "FOOTBALL");
        
        entity.setHomeScore(2);
        entity.setAwayScore(1);
        
        assertEquals(Integer.valueOf(2), entity.getHomeScore());
        assertEquals(Integer.valueOf(1), entity.getAwayScore());
    }
}