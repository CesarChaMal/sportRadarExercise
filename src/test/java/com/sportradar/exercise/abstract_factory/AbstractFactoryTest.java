package com.sportradar.exercise.abstract_factory;

import com.sportradar.exercise.match.*;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.strategy.ScoringStrategy;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategiesFunctional2;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategyType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import static com.sportradar.exercise.strategy_functionall2.ScoringStrategiesFunctional2.STRATEGY_MAP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AbstractFactoryTest {

    private FootballMatchFactory footballMatchFactory;
    private BasketballMatchFactory basketballMatchFactory;

    @Before
    public void setUp() {
        footballMatchFactory = new FootballMatchFactory();
        basketballMatchFactory = new BasketballMatchFactory();
    }

    @Test
    public void testInitialMatchState() {
        Team<?> homeTeam = mock(Team.class);
        Team<?> awayTeam = mock(Team.class);

        Match footballMatch = footballMatchFactory.createMatchBuilder(homeTeam, awayTeam).build();
        Match basketballMatch = basketballMatchFactory.createMatchBuilder(homeTeam, awayTeam).build();

        assertEquals("Football match should start in 'Not Started' state", MatchState.forNotStartedState(), footballMatch.getState());
        assertEquals("Basketball match should start in 'Not Started' state", MatchState.forNotStartedState(), basketballMatch.getState());
        assertTrue("Football match should start in 'Not Started' state", footballMatch.getState() == MatchState.forNotStartedState());
        assertTrue("Basketball match should start in 'Not Started' state", basketballMatch.getState() == MatchState.forNotStartedState());
    }

    @Test
    public void testEventManagerIntegration() {
        FootballTeam homeFootballTeam = new FootballTeam.Builder().name("Home Football Team").build();
        FootballMatch footballMatch = footballMatchFactory.createMatchBuilder(homeFootballTeam, homeFootballTeam).build();

        footballMatch.addEvent(EventType.GOAL, new ArrayList<>());
        assertEquals("Event manager should handle events correctly", 1, footballMatch.getEvents().size());
    }

    @Test
    public void testScoringModesSwitching() {
        FootballTeam homeFootballTeam = new FootballTeam.Builder().name("Home Team").build();
        FootballMatch match = (FootballMatch) footballMatchFactory.createMatchBuilder(homeFootballTeam, homeFootballTeam)
                .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
                .state(MatchState.forInProgressState())
                .build();

//        match.setEnableValidationOfStrategyMode(true);
        match.setScoringStrategy(ScoringStrategy.forFootballNormalTime());
        match.updateScore(EventType.SCORE_UPDATE, 1, 0);
        assertEquals("Score should update correctly in CLASSIC mode", 1, match.getHomeScore());

        match.setStrategyMode(ScoringStrategyMode.FUNCTIONAL1);
/*
        match.setScoringStrategyFunctional1((m, scores) -> {
            m.setHomeScore(scores[0]);
            m.setAwayScore(scores[1]);
        });
*/
        match.setScoringStrategyFunctional1(STRATEGY_MAP.get(ScoringStrategyType.FOOTBALL_NORMAL_TIME));
        match.getScoringStrategyFunctional1().accept(match, new int[]{2, 2});
        assertEquals("Home score should be updated in FUNCTIONAL1 mode", 2, match.getHomeScore());
    }

    @Test
    public void testStrategyChangeAfterCreation() {
        // Setup: Create a match with a specific initial state.
        FootballTeam homeFootballTeam = new FootballTeam.Builder().name("Team A").build();
        FootballMatch match = (FootballMatch) footballMatchFactory.createMatchBuilder(homeFootballTeam, homeFootballTeam)
                .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
                .state(MatchState.forInProgressState())
                .build();

        match.updateScore(EventType.SCORE_UPDATE, 2, 2);
        assertEquals("Initial scores should be set", 2, match.getHomeScore());

//        match.setEnableValidationOfStrategyMode(true);
        match.setStrategyMode(ScoringStrategyMode.FUNCTIONAL1);
/*
        match.setScoringStrategyFunctional1((m, scores) -> {
            m.setHomeScore(scores[0]);
            m.setAwayScore(scores[1]);
        });
*/
        match.setScoringStrategyFunctional1(STRATEGY_MAP.get(ScoringStrategyType.FOOTBALL_NORMAL_TIME));
        match.getScoringStrategyFunctional1().accept(match, new int[]{match.getHomeScore() + 1, match.getAwayScore() + 1});

        assertEquals("Scores should be updated correctly in FUNCTIONAL1 mode", 3, match.getHomeScore());

        match.setStrategyMode(ScoringStrategyMode.FUNCTIONAL2);
        match.setScoringStrategyFunctional2(ScoringStrategyType.FOOTBALL_NORMAL_TIME);
        BiConsumer<Match, int[]> scoringStrategy = ScoringStrategiesFunctional2.getStrategy(ScoringStrategyType.FOOTBALL_NORMAL_TIME);
        scoringStrategy.accept(match, new int[]{match.getHomeScore() + 1, match.getAwayScore() + 1});

        assertEquals("Home score should be adjusted correctly after strategy change", 4, match.getHomeScore());
        assertEquals("Away score should be adjusted correctly after strategy change", 4, match.getAwayScore());
    }

    @Test
    public void testFootballMatchFactoryAppliesCorrectStrategy() {
//        Team<FootballPlayer> homeFootballTeam = FootballTeam.builder().name("Home Football Team").build();
//        Team<FootballPlayer> awayFootballTeam = FootballTeam.builder().name("Away Football Team").build();
//        Match match = footballMatchFactory.createMatchBuilder(homeFootballTeam, awayFootballTeam).build();

        FootballTeam homeFootballTeam = FootballTeam.builder().name("Home Football Team").build();
        FootballTeam awayFootballTeam = FootballTeam.builder().name("Away Football Team").build();

        FootballMatch match = footballMatchFactory.createMatchBuilder(homeFootballTeam, awayFootballTeam).build();

        match.getScoringStrategyFunctional1().accept(match, new int[]{1, 2});
        assertEquals("Home score should be updated correctly for football", 1, match.getHomeScore());
        assertEquals("Away score should be updated correctly for football", 2, match.getAwayScore());
    }

    @Test
    public void testBasketballMatchFactoryAppliesCorrectStrategy() {
        BasketballTeam homeBasketballTeam = BasketballTeam.builder().name("Home Basketball Team").build();
        BasketballTeam awayBasketballTeam = BasketballTeam.builder().name("Away Basketball Team").build();

        BasketballMatch match = basketballMatchFactory.createMatchBuilder(homeBasketballTeam, awayBasketballTeam).build();
        match.getScoringStrategyFunctional1().accept(match, new int[]{10, 20});
        assertEquals("Home score should be updated correctly for basketball", 10, match.getHomeScore());
        assertEquals("Away score should be updated correctly for basketball", 20, match.getAwayScore());
    }
}