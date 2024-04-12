package com.sportradar.exercise.observer;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.match.*;
import com.sportradar.exercise.state.MatchState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatchObserverIntegrationTest {

    private FootballMatch match;
    private TestObserver observer;

    private class TestObserver implements Observer<MatchChangeEvent> {
        private int updateCount = 0;

        @Override
        public void update(MatchChangeEvent event) {
            updateCount++;
            System.out.println("Update received: Match state updated in the observer.");
            assertNotNull("Event should not be null", event);
            assertSame("Event should contain the correct match", match, event.getMatch());
        }

        public int getUpdateCount() {
            return updateCount;
        }
    }

    @Before
    public void setUp() {
        Team<FootballPlayer> homeTeam = new FootballTeam.Builder().name("Home Team").build();
        Team<FootballPlayer> awayTeam = new FootballTeam.Builder().name("Away Team").build();

        FootballMatchFactory factory = new FootballMatchFactory();
        match = factory.createMatchBuilder(homeTeam, awayTeam).build();

        observer = new TestObserver();
        match.registerObserver(observer);
    }

    @Test
    public void testObserverReceivesMatchChangeEventOnScoreUpdate() {
        match.setState(MatchState.forInProgressState());

        // Triggering the observer by updating the score
        match.updateScore(1, 0);

        // Asserting that the observer's update method was called exactly once
        assertEquals("Observer should have been updated exactly once", 1, observer.getUpdateCount());
    }
}
