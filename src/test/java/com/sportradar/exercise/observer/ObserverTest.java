package com.sportradar.exercise.observer;

import com.sportradar.exercise.match.Match;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class ObserverTest {

    private MatchEventNotifier<MatchEvent> matchEventNotifier;
    private Observer<MatchEvent> observer;
    private Match match;
    private MatchEvent matchEvent;

    @Before
    public void setUp() {
        matchEventNotifier = new MatchEventNotifier<>();
        observer = Mockito.mock(Observer.class);
        match = Mockito.mock(Match.class);
        matchEvent = new MatchEvent(match);
    }

    @Test
    public void testRegisterObserver() {
        matchEventNotifier.registerObserver(observer);
        matchEventNotifier.notifyObservers(matchEvent);
        verify(observer, times(1)).update(matchEvent);
    }

    @Test
    public void testRemoveObserver() {
        matchEventNotifier.registerObserver(observer);
        matchEventNotifier.removeObserver(observer);
        matchEventNotifier.notifyObservers(matchEvent);
        verify(observer, never()).update(matchEvent);
    }

    @Test
    public void testNotifyObservers() {
        Observer<MatchEvent> secondObserver = Mockito.mock(Observer.class);
        matchEventNotifier.registerObserver(observer);
        matchEventNotifier.registerObserver(secondObserver);
        
        matchEventNotifier.notifyObservers(matchEvent);
        
        verify(observer, times(1)).update(matchEvent);
        verify(secondObserver, times(1)).update(matchEvent);
    }
}
