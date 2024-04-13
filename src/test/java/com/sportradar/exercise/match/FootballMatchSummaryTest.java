package com.sportradar.exercise.match;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class FootballMatchSummaryTest {
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
    public void testGoalEvents() {
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
    public void testYellowCardEvents() {
        FootballMatch match = createMatch();
        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
        match.addEvent(EventType.YELLOW_CARD, Collections.singletonList(england.getRoster().get(0))); // Yellow card to Peter Shilton
        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());

        assertEquals(3, match.getEvents().size());
        assertEquals(EventType.YELLOW_CARD, match.getEvents().get(1).getEventType());
        assertEquals("Peter Shilton", match.getEvents().get(1).getInvolvedPlayers().get(0).getName());
    }

    @Test
    public void testRedCardEvents() {
        FootballMatch match = createMatch();
        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
        match.addEvent(EventType.RED_CARD, Collections.singletonList(argentina.getRoster().get(0))); // Red card to Diego Maradona
        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());

        assertEquals(3, match.getEvents().size());
        assertEquals(EventType.RED_CARD, match.getEvents().get(1).getEventType());
        assertEquals("Diego Maradona", match.getEvents().get(1).getInvolvedPlayers().get(0).getName());
    }

    @Test
    public void testSubstitutionEvents() {
        FootballMatch match = createMatch();
        // Adding a second player for substitution test
        FootballPlayer substitute = new FootballPlayer.Builder().name("Claudio Caniggia").goalsScored(0).build();
        argentina.addPlayer(substitute);

        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
        match.addEvent(EventType.SUBSTITUTION, Arrays.asList(argentina.getRoster().get(0), substitute)); // Substitution Maradona out, Caniggia in
        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());

        assertEquals(3, match.getEvents().size());
        assertEquals(EventType.SUBSTITUTION, match.getEvents().get(1).getEventType());
        assertEquals("Diego Maradona", match.getEvents().get(1).getInvolvedPlayers().get(0).getName());
        assertEquals("Claudio Caniggia", match.getEvents().get(1).getInvolvedPlayers().get(1).getName());
    }

    @Test
    public void testPenaltyEvent() {
        FootballMatch match = createMatch();
        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
        match.addEvent(EventType.PENALTY, Collections.singletonList(argentina.getRoster().get(0))); // Penalty awarded to Diego Maradona
        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());

        assertEquals(3, match.getEvents().size());
        assertEquals(EventType.PENALTY, match.getEvents().get(1).getEventType());
        assertEquals("Diego Maradona", match.getEvents().get(1).getInvolvedPlayers().get(0).getName());
    }

    private FootballMatch createMatch() {
        return factory.createMatchBuilder(argentina, england).build();
    }
}
