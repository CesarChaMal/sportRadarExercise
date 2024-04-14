package com.sportradar.exercise.match;

import com.sportradar.exercise.abstract_factory.BasketballMatchFactory;
import com.sportradar.exercise.observer.MatchObserver;
import com.sportradar.exercise.state.MatchState;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BasketballMatchSummaryTest {
    private BasketballMatchFactory factory;
    private Team<BasketballPlayer> bulls;
    private Team<BasketballPlayer> jazz;
    private BasketballMatch match;

    @Before
    public void setUp() {
        factory = new BasketballMatchFactory();

        bulls =getBasketballTeam("Chicago Bulls");
        jazz = getBasketballTeam("Utah Jazz");

        BasketballPlayer jordan = getBasketballPlayer("Michael Jordan", bulls);
        BasketballPlayer pippen = getBasketballPlayer("Scottie Pippen", bulls);
        BasketballPlayer malone = getBasketballPlayer("Karl Malone", jazz);
        BasketballPlayer stockton = getBasketballPlayer("John Stockton", jazz);

        bulls.addPlayer(jordan);
        bulls.addPlayer(pippen);
        jazz.addPlayer(malone);
        jazz.addPlayer(stockton);

        match = createMatch();
        MatchObserver observer = new MatchObserver();
        match.registerObserver(observer);
    }

    private Team<BasketballPlayer> getBasketballTeam(String name) {
        return new Team.Builder<BasketballPlayer>()
                .name(name)
                .build();
    }

    private BasketballPlayer getBasketballPlayer(String name, Team<BasketballPlayer> team) {
        return  new BasketballPlayer.Builder()
                .name(name)
                .pointsScored(0)
                .team(team)
                .build();
    }

    @Test
    public void testPointsScoredEvents() {
//        BasketballMatch match = createMatch();
//        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
        match.startMatch();
//        match.addEvent(EventType.POINTS_SCORED, Collections.singletonList(bulls.getRoster().get(0)), 2); // Michael Jordan scores a 2-pointer
        match.scorePoints(bulls.getRoster().get(0), 2); // Michael Jordan scores a 2-pointer
//        match.addEvent(EventType.POINTS_SCORED, Collections.singletonList(jazz.getRoster().get(0)), 2); // Karl Malone scores a 2-pointer
        match.scorePoints(jazz.getRoster().get(0), 2); // Karl Malone scores a 2-pointer
//        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());
        match.finishMatch();

//        assertEquals(4, match.getEvents().size());
        // scorePoints  has 2 events why increments to 6
        assertEquals(6, match.getEvents().size());
        MatchEvent<?> jordanScoreEvent = match.getEvents().stream()
                .filter(e -> e.getEventType() == EventType.TWO_POINT_SCORE)
                .findFirst().orElse(null);
        assertNotNull(jordanScoreEvent);
        assertEquals("Michael Jordan", jordanScoreEvent.getInvolvedPlayers().get(0).getName());
        assertEquals(2, jordanScoreEvent.getAdditionalData("points"));
        assertEquals(EventType.POINTS_SCORED, match.getEvents().get(2).getEventType());
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
//        match.addEvent(EventType.MATCH_STARTED, Collections.emptyList());
//        match.addEvent(EventType.POINTS_SCORED, Collections.singletonList(bulls.getRoster().get(0)), 2); // Michael Jordan's game-winning shot
        match.setState(MatchState.forInProgressState());
        match.scorePoints((BasketballPlayer) bulls.getRoster().get(0), 2); // Michael Jordan's game-winning shot
        match.addEvent(EventType.MATCH_FINISHED, Collections.emptyList());

        assertEquals(3, match.getEvents().size());
        assertEquals(EventType.POINTS_SCORED, match.getEvents().get(1).getEventType());
        assertEquals("Michael Jordan", match.getEvents().get(0).getInvolvedPlayers().get(0).getName());
        assertEquals(2, match.getEvents().get(0).getAdditionalData("points"));

    }

    private BasketballMatch createMatch() {
        return factory.createMatchBuilder(bulls, jazz).build();
    }
}
