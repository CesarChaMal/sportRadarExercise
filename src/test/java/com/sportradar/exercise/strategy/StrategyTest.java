package com.sportradar.exercise.strategy;

import com.sportradar.exercise.abstract_factory.BasketballMatchFactory;
import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.EventType;
import com.sportradar.exercise.match.FootballTeam;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.state.MatchState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrategyTest {

    private Match createMatchWithStrategy(MatchFactory factory, ScoringStrategy strategy) {
        Team<?> teamA = new FootballTeam.Builder().name("Team A").build();
        Team<?> teamB = new FootballTeam.Builder().name("Team B").build();

        return factory.createMatchBuilder(teamA, teamB)
                .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
                .state(MatchState.forInProgressState())
                .scoringStrategy(strategy)
                .build();
    }

    @Test
    public void testFootballNormalTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new FootballMatchFactory(), ScoringStrategy.forFootballNormalTime());
        match.getScoringStrategy().calculateScore(match, 3, 3);
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testFootballExtraTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new FootballMatchFactory(), ScoringStrategy.forFootballExtraTime());
        match.updateScore(EventType.SCORE_UPDATE, 2, 2);
        match.getScoringStrategy().calculateScore(match, 1, 1);
        assertEquals("Home score should be 3", 3, match.getHomeScore());
        assertEquals("Away score should be 3", 3, match.getAwayScore());
    }

    @Test
    public void testBasketballExtraTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new BasketballMatchFactory(), ScoringStrategy.forBasketballExtraTime());
        match.updateScore(EventType.SCORE_UPDATE, 20, 20);
        match.getScoringStrategy().calculateScore(match, 10, 5);
        assertEquals("Home score should be 30", 30, match.getHomeScore());
        assertEquals("Away score should be 25", 25, match.getAwayScore());
    }

    @Test
    public void testBasketballNormalTimeScoringStrategy() {
        Match match = createMatchWithStrategy(new BasketballMatchFactory(), ScoringStrategy.forBasketballNormalTime());
        match.getScoringStrategy().calculateScore(match, 10, 5);
        match.updateScore(EventType.SCORE_UPDATE, 10, 5);
        assertEquals("Home score should be 10", 10, match.getHomeScore());
        assertEquals("Away score should be 5", 5, match.getAwayScore());
    }
}
