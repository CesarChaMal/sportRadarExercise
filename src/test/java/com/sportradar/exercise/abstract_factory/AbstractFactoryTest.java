package com.sportradar.exercise.abstract_factory;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.strategy.BasketballNormalTimeScoringStrategy;
import com.sportradar.exercise.strategy.FootballNormalTimeScoringStrategy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AbstractFactoryTest {

    private MatchFactory footballMatchFactory;
    private MatchFactory basketballMatchFactory;

    @Before
    public void setUp() {
        footballMatchFactory = new FootballMatchFactory();
        basketballMatchFactory = new BasketballMatchFactory();
    }

    @Test
    public void testFootballMatchFactoryCreatesCorrectMatch() {
        Match match = footballMatchFactory.createMatchBuilder("Home Football Team", "Away Football Team").build();

        assertTrue("Scoring strategy should be instance of FootballNormalTimeScoringStrategy",
                match.getScoringStrategy() instanceof FootballNormalTimeScoringStrategy);
    }

    @Test
    public void testBasketballMatchFactoryCreatesCorrectMatch() {
        Match match = basketballMatchFactory.createMatchBuilder("Home Basketball Team", "Away Basketball Team").build();

        assertTrue("Scoring strategy should be instance of BasketballScoringStrategy",
                match.getScoringStrategy() instanceof BasketballNormalTimeScoringStrategy);
    }
}