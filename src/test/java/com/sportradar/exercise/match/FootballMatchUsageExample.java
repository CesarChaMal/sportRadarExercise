package com.sportradar.exercise.match;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.observer.MatchObserver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FootballMatchUsageExample {
    private FootballMatchFactory factory;
    private Team<FootballPlayer> homeTeam;
    private Team<FootballPlayer> awayTeam;
    private FootballMatch match;
    private MatchObserver observer;

    @Before
    public void setUp() {
        // Initialize factory and create teams
        factory = new FootballMatchFactory();
        homeTeam = new Team.Builder<FootballPlayer>().name("Team A").build();
        awayTeam = new Team.Builder<FootballPlayer>().name("Team B").build();

        // Add players to teams
        homeTeam.addPlayer(new FootballPlayer.Builder().name("John Doe").team(homeTeam).build());
        awayTeam.addPlayer(new FootballPlayer.Builder().name("Joe Smith").team(awayTeam).build());

        // Create the match and register an observer
        match = factory.createMatchBuilder(homeTeam, awayTeam).build();
        observer = new MatchObserver();
        match.registerObserver(observer);
    }

    @Test
    public void testMatchFlow() {
        // Start the match
        match.startMatch();

        // Simulate scoring events
        match.scoreGoal(homeTeam.getRoster().get(0), null);
        match.scoreGoal(homeTeam.getRoster().get(0), null);

        // End the match
        match.finishMatch();

        // Assertions to ensure the match flowed as expected
        assertEquals("Expected number of events should be correct", 6, match.getEvents().size());
        MatchEvent<?> firstGoalEvent = match.getEvents().stream()
                .filter(e -> e.getEventType() == EventType.GOAL)
                .findFirst()
                .orElse(null);

        assertNotNull("Goal event should exist", firstGoalEvent);
        assertEquals("First goal should be scored by Maradona", "John Doe", firstGoalEvent.getInvolvedPlayers().get(0).getName());
        assertTrue("Observer should have received the event", observer.isEventReceived());
    }
}
