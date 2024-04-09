package com.sportradar.exercise.observer;

import com.sportradar.exercise.match.Match;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ObserverTest {

    private Observer observer;
    private Match match;

    @Before
    public void setUp() {
        match = new Match.Builder("Home Team", "Away Team").build();
        observer = mock(Observer.class);
    }

    @Test
    public void testRegisterObserverAndNotify() {
        match.registerObserver(observer);
        match.notifyObservers();
        verify(observer, times(1)).update(match);
    }
}
