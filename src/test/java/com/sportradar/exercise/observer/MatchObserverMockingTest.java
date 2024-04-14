package com.sportradar.exercise.observer;

import com.sportradar.exercise.match.EventManager;
import com.sportradar.exercise.match.EventType;
import com.sportradar.exercise.match.FootballMatch;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.state.MatchState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.mockito.Mockito.*;

public class MatchObserverMockingTest {

    private FootballMatch match;
    private Observer<MatchChangeEvent> observer;
    private MatchChangeEvent matchEvent1;
    private MatchChangeEvent matchEvent2;
    private MatchEventNotifier<MatchChangeEvent> notifier;
    private EventManager eventManager;

    @Before
    public void setUp() {
        match = mock(FootballMatch.class);
        eventManager = mock(EventManager.class);
        notifier = new MatchEventNotifier<>(MatchChangeEvent.class);
        observer = mock(Observer.class);
        matchEvent1 = new MatchChangeEvent(match, EventType.MATCH_STARTED);

        Team<?> homeTeam = mock(Team.class);
        Team<?> awayTeam = mock(Team.class);

        when(match.getEventManager()).thenReturn(eventManager);
        when(eventManager.getMatchEventNotifier()).thenReturn(notifier);
        when(match.getState()).thenReturn(MatchState.IN_PROGRESS);
//        doNothing().when(observer).update(any(MatchChangeEvent.class));

        notifier.registerObserver(observer);
        notifier.notifyObservers(matchEvent1);
    }

    @Test
    public void testObserverReceivesMatchChangeEventOnScoreUpdate() {
        match.updateScore(EventType.SCORE_UPDATE, 1, 0);
        matchEvent2 = new MatchChangeEvent(match, EventType.SCORE_UPDATE);
        notifier.notifyObservers(matchEvent2);

        ArgumentCaptor<MatchChangeEvent> captor = ArgumentCaptor.forClass(MatchChangeEvent.class);
//        verify(observer).update(captor.capture());
//        verify(observer, atLeastOnce()).update(captor.capture());
//        verify(observer, atLeast(0)).update(captor.capture());
        verify(observer, times(2)).update(captor.capture());

        List<MatchChangeEvent> capturedEvents = captor.getAllValues();
//        for (MatchChangeEvent event : capturedEvents) {
//            System.out.println(event.getEventType());
//        }
//        capturedEvents.forEach(event -> System.out.println(event.getEventType()));
        capturedEvents.forEach(event -> System.out.println(event.eventType()));

        verify(observer, atLeastOnce()).update(matchEvent1);
        verify(observer, atLeastOnce()).update(matchEvent2);
    }
}

