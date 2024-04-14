package com.sportradar.exercise.observer;

import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.*;
import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.strategy.ScoringStrategy;
import com.sportradar.exercise.strategy.ScoringStrategyMode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class MatchObserverTest {

    private FootballMatch match;
    private MatchObserver observer;

    @Before
    public void setUp() {
        Team<FootballPlayer> homeTeam = new FootballTeam.Builder().name("Home Football Team").build();
        Team<FootballPlayer> awayTeam = new FootballTeam.Builder().name("Away Football Team").build();

        MatchFactory<FootballMatch> factory = new FootballMatchFactory();
        match = (FootballMatch) factory.createMatchBuilder(homeTeam, awayTeam)
                       .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
                       .build();

        observer = new MatchObserver();
        match.registerObserver(observer);
    }

    @Test
    public void testObserverReceivesGoalEvent() {
        FootballPlayer scorer = new FootballPlayer.Builder().name("John Doe").team(match.getHomeTeam()).build();
        FootballPlayer assistant = new FootballPlayer.Builder().name("Jane Doe").team(match.getHomeTeam()).build();
        match.setState(MatchState.IN_PROGRESS);
        match.scoreGoal(scorer, assistant);

        match.notifyObservers(new MatchChangeEvent(match, EventType.GOAL));

        assertTrue("Observer should have received the event", observer.isEventReceived());
    }
}
