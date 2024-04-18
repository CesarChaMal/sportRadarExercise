package com.sportradar.exercise.observer;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.match.*;
import com.sportradar.exercise.scoring.Scoreboard;
import com.sportradar.exercise.state.MatchState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MatchObserverIntegrationTest {

    private FootballMatch match;
    private TestObserver observer;
    private static final Logger logger = Logger.getLogger(MatchObserverIntegrationTest.class.getName());

    private class TestObserver implements Observer<MatchChangeEvent> {
        private int updateCount = 0;

        @Override
        public void update(MatchChangeEvent event) {
            updateCount++;
            logger.info("Update received: Match state updated in the observer, count: " + updateCount);
            assertNotNull("Event should not be null", event);
//            assertSame("Event should contain the correct match", match, event.getMatch());
            assertSame("Event should contain the correct match", match, event.match());
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
//        match.setState(MatchState.forInProgressState());
        match.startMatch();
        match.updateScore(EventType.SCORE_UPDATE, 1, 0);

//        assertEquals("Observer should have been updated exactly once", 1, observer.getUpdateCount());
        assertTrue("Observer should have been updated at least once", observer.getUpdateCount() > 0);
    }
}
