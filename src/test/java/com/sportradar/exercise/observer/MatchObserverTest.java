package com.sportradar.exercise.observer;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.*;
import com.sportradar.exercise.state.MatchState;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MatchObserverTest {

    private Match match;
    private Observer<MatchChangeEvent> observer;

    private Team<?> homeTeam;
    private Team<?> awayTeam;

    @Before
    public void setUp() {
        homeTeam = mock(FootballTeam.class);
        awayTeam = mock(FootballTeam.class);

        FootballMatchFactory factory = new FootballMatchFactory();
        match = factory.createMatchBuilder(homeTeam, awayTeam).build();
        observer = mock(Observer.class);
        match.registerObserver(observer);
    }

    @Test
    public void testObserverReceivesMatchChangeEventOnScoreUpdate() {
        when(match.getState().canUpdateScore()).thenReturn(true);
        match.setState(MatchState.forInProgressState());

        match.updateScore(1, 0);

//        verify(observer, atLeastOnce()).update(any(MatchChangeEvent.class));
        verify(observer, times(1)).update(any(MatchChangeEvent.class));
//        verify(observer).update(any(MatchChangeEvent.class));
    }
}
