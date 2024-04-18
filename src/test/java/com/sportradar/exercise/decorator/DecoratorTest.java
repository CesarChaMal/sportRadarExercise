package com.sportradar.exercise.decorator;

import com.sportradar.exercise.match.*;
import com.sportradar.exercise.strategy.ScoringStrategy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DecoratorTest {

    private Team<FootballPlayer> footballTeam;
    private Team<BasketballPlayer> basketballTeam;
    private FootballMatch footballMatch;
    private BasketballMatch basketballMatch;
    private FootballPlayer footballPlayer;
    private BasketballPlayer basketballPlayer;

    @Before
    public void setUp() {
        footballTeam = new Team.Builder<FootballPlayer>().name("Football Team").build();
        basketballTeam = new Team.Builder<BasketballPlayer>().name("Basketball Team").build();

        footballPlayer = new FootballPlayer.Builder().name("Football Player").team(footballTeam).build();
        basketballPlayer = new BasketballPlayer.Builder().name("Basketball Player").team(basketballTeam).build();

        footballMatch = new FootballMatch.Builder(footballTeam, new Team.Builder<FootballPlayer>().name("Opponent Team").build()).eventManagerFactory(FootballEventManager::new).build();
        basketballMatch = new BasketballMatch.Builder(basketballTeam, new Team.Builder<BasketballPlayer>().name("Opponent Team").build()).eventManagerFactory(BasketballEventManager::new).build();
    }

    @Test
    public void testFootballOvertimeScoring() {
        footballMatch.startMatch();
        footballMatch.updateScore(EventType.SCORE_UPDATE, 1, 1);

        MatchOverTime footballOvertime = new MatchOverTime.Builder(footballMatch)
                .withOvertimeScoringStrategy(ScoringStrategy.forFootballExtraTime())
                .build();

        footballOvertime.startOvertime();
        footballMatch.scoreGoal(footballPlayer, null);

        assertEquals("Score should reflect extra-time scoring", 2, footballMatch.getHomeScore());
    }

    @Test
    public void testBasketballOvertimeScoring() {
        basketballMatch.startMatch();
        basketballMatch.updateScore(EventType.SCORE_UPDATE, 100, 100);

        MatchOverTime basketballOvertime = new MatchOverTime.Builder(basketballMatch)
                .withOvertimeScoringStrategy(ScoringStrategy.forBasketballExtraTime())
                .build();

        basketballOvertime.startOvertime();
        basketballMatch.scorePoints(basketballPlayer, 3);

        assertEquals("Score should be normal during overtime", 103, basketballMatch.getHomeScore());
    }
}
