package com.sportradar.exercise.abstract_factory;

import com.sportradar.exercise.match.Match;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AbstractFactoryTest {

    private MatchFactory footballMatchFactory;
    private MatchFactory basketballMatchFactory;

    @Before
    public void setUp() {
        footballMatchFactory = new FootballMatchFactory();
        basketballMatchFactory = new BasketballMatchFactory();
    }

/*
    @Test
    public void testFootballMatchFactoryCreatesCorrectMatch() {
        Match match = footballMatchFactory.createMatchBuilder("Home Football Team", "Away Football Team").build();
        assertTrue("Scoring strategy should be instance of FootballNormalTimeScoringStrategy", match.getScoringStrategy() instanceof FootballNormalTimeScoringStrategy);
    }
*/

/*
    @Test
    public void testBasketballMatchFactoryCreatesCorrectMatch() {
        Match match = basketballMatchFactory.createMatchBuilder("Home Basketball Team", "Away Basketball Team").build();
        assertTrue("Scoring strategy should be instance of BasketballScoringStrategy", match.getScoringStrategy() instanceof BasketballNormalTimeScoringStrategy);
    }
*/

    @Test
    public void testFootballMatchFactoryAppliesCorrectStrategy() {
        Match match = footballMatchFactory.createMatchBuilder("Home Football Team", "Away Football Team").build();
        match.getScoringStrategyFunctional1().accept(match, new int[]{1, 2});
        assertEquals("Home score should be updated correctly for football", 1, match.getHomeScore());
        assertEquals("Away score should be updated correctly for football", 2, match.getAwayScore());
    }

    @Test
    public void testBasketballMatchFactoryAppliesCorrectStrategy() {
        Match match = basketballMatchFactory.createMatchBuilder("Home Basketball Team", "Away Basketball Team").build();
        match.getScoringStrategyFunctional1().accept(match, new int[]{10, 20});
        assertEquals("Home score should be updated correctly for basketball", 10, match.getHomeScore());
        assertEquals("Away score should be updated correctly for basketball", 20, match.getAwayScore());
    }
}