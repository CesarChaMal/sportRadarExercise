package com.sportradar.exercise.match;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class MatchSummaryTest {
    private FootballMatchFactory factory;
    private Team<FootballPlayer> argentina;
    private Team<FootballPlayer> england;

    @Before
    public void setUp() {
        factory = new FootballMatchFactory();

        argentina = new Team.Builder<FootballPlayer>()
                .name("Argentina")
                .addPlayer(new FootballPlayer.Builder().name("Diego Maradona").goalsScored(0).build())
                .build();

        england = new Team.Builder<FootballPlayer>()
                .name("England")
                .addPlayer(new FootballPlayer.Builder().name("Peter Shilton").goalsScored(0).build())
                .build();
    }

    @Test
    public void testMatchEvents() {
        FootballMatch match = createMatch();
        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
        match.addEvent(EventType.GOAL, Collections.singletonList(argentina.getRoster().get(0))); // Hand of God
        match.addEvent(EventType.GOAL, Collections.singletonList(argentina.getRoster().get(0))); // Goal of the Century
        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());

        assertEquals(4, match.getEvents().size());
        assertEquals(EventType.GOAL, match.getEvents().get(1).getEventType());
        assertEquals("Diego Maradona", match.getEvents().get(1).getInvolvedPlayers().get(0).getName());
    }

    @Test
    public void testMatchEvents2() {
        FootballMatch match = createMatch();
        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
        match.scoreHomeGoal(argentina.getRoster().get(0), null); // Hand of God
        match.scoreHomeGoal(argentina.getRoster().get(0), null); // Goal of the Century
        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());

        assertEquals(4, match.getEvents().size());
        assertEquals(EventType.GOAL, match.getEvents().get(1).getEventType());
        assertEquals("Diego Maradona", match.getEvents().get(1).getInvolvedPlayers().get(0).getName());
    }

    private FootballMatch createMatch() {
        return factory.createMatchBuilder(argentina, england).build();
    }
}
