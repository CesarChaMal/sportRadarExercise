package com.sportradar.exercise.match;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.observer.MatchObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FootballMatchSummaryTest {
    private FootballMatchFactory factory;
    private Team<FootballPlayer> argentina;
    private Team<FootballPlayer> england;
    private FootballMatch match;

    @Before
    public void setUp() {
        factory = new FootballMatchFactory();

        argentina = getFootballTeam("Argentina");
        england = getFootballTeam("England");

        FootballPlayer maradona = getFootballPlayer("Diego Maradona", argentina);
        FootballPlayer shilton = getFootballPlayer("Peter Shilton", england);

        argentina.addPlayer(maradona);
        england.addPlayer(shilton);

         match = createMatch();
        MatchObserver observer = new MatchObserver();
        match.registerObserver(observer);
    }

    private Team<FootballPlayer> getFootballTeam(String name) {
        return new Team.Builder<FootballPlayer>()
                .name(name)
                .build();
    }

    private FootballPlayer getFootballPlayer(String name, Team<FootballPlayer> team) {
        return new FootballPlayer.Builder()
                .name(name)
                .goalsScored(0)
                .team(team)
                .build();
    }

    @Test
    public void testGoalEvents() {
//        FootballMatch match = createMatch();
//        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
        match.startMatch();
//        match.addEvent(EventType.GOAL, Collections.singletonList(argentina.getRoster().get(0))); // Hand of God
        match.scoreGoal(argentina.getRoster().get(0),  null); // Hand of God
//        match.addEvent(EventType.GOAL, Collections.singletonList(argentina.getRoster().get(0))); // Goal of the Century
        match.scoreGoal(argentina.getRoster().get(0), null); // Goal of the Century
//        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());
        match.finishMatch();

//        assertEquals(4, match.getEvents().size());
        // scoreGoal has 2 events why increments to 6
        assertEquals(6, match.getEvents().size());
        MatchEvent<?> firstGoalEvent = match.getEvents().stream()
                .filter(e -> e.getEventType() == EventType.GOAL)
                .findFirst().orElse(null);
        assertNotNull(firstGoalEvent);
        assertEquals("Diego Maradona", firstGoalEvent.getInvolvedPlayers().get(0).getName());
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
