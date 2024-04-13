package com.sportradar.exercise.match;

import com.sportradar.exercise.abstract_factory.BasketballMatchFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class BasketballMatchSummaryTest {
    private BasketballMatchFactory factory;
    private Team<BasketballPlayer> bulls;
    private Team<BasketballPlayer> jazz;

    @Before
    public void setUp() {
        factory = new BasketballMatchFactory();

        bulls = new Team.Builder<BasketballPlayer>()
                .name("Chicago Bulls")
                .addPlayer(new BasketballPlayer.Builder().name("Michael Jordan").pointsScored(0).build())
                .addPlayer(new BasketballPlayer.Builder().name("Scottie Pippen").pointsScored(0).build())
                .build();

        jazz = new Team.Builder<BasketballPlayer>()
                .name("Utah Jazz")
                .addPlayer(new BasketballPlayer.Builder().name("Karl Malone").pointsScored(0).build())
                .addPlayer(new BasketballPlayer.Builder().name("John Stockton").pointsScored(0).build())
                .build();
    }

    @Test
    public void testPointsScoredEvents() {
        BasketballMatch match = createMatch();
        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
        match.addEvent(EventType.POINTS_SCORED, Collections.singletonList(bulls.getRoster().get(0)), 2); // Michael Jordan scores a 2-pointer
//        match.scorePoints(bulls.getRoster().get(0), 2); // Michael Jordan scores a 2-pointer
        match.addEvent(EventType.POINTS_SCORED, Collections.singletonList(jazz.getRoster().get(0)), 2); // Karl Malone scores a 2-pointer
//        match.scorePoints(jazz.getRoster().get(0), 2); // Karl Malone scores a 2-pointer
        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());

        assertEquals(4, match.getEvents().size());
        assertEquals(EventType.POINTS_SCORED, match.getEvents().get(1).getEventType());
        assertEquals("Michael Jordan", match.getEvents().get(1).getInvolvedPlayers().get(0).getName());
        assertEquals(2, match.getEvents().get(1).getAdditionalData("points"));
    }

    @Test
    public void testFoulEvents() {
        BasketballMatch match = createMatch();
        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
        match.addEvent(EventType.FOUL, Collections.singletonList(jazz.getRoster().get(0))); // Karl Malone commits a foul
        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());

        assertEquals(3, match.getEvents().size());
        assertEquals(EventType.FOUL, match.getEvents().get(1).getEventType());
        assertEquals("Karl Malone", match.getEvents().get(1).getInvolvedPlayers().get(0).getName());
    }

    @Test
    public void testFinalShotEvent() {
        BasketballMatch match = createMatch();
        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
//        match.addEvent(EventType.POINTS_SCORED, Collections.singletonList(bulls.getRoster().get(0)), 2); // Michael Jordan's game-winning shot
        match.scorePoints((BasketballPlayer) bulls.getRoster().get(0), 2); // Michael Jordan's game-winning shot
        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());

        assertEquals(3, match.getEvents().size());
        assertEquals(EventType.POINTS_SCORED, match.getEvents().get(1).getEventType());
        assertEquals("Michael Jordan", match.getEvents().get(1).getInvolvedPlayers().get(0).getName());
        assertEquals(2, match.getEvents().get(1).getAdditionalData("points"));
    }

    private BasketballMatch createMatch() {
        return factory.createMatchBuilder(bulls, jazz).build();
    }
}
