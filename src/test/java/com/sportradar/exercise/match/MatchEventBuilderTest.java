package com.sportradar.exercise.match;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import org.junit.Test;

import java.time.Instant;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MatchEventBuilderTest {

    @Test
    public void matchEventBuilderCreatesCorrectEvent() {
        MatchFactory<FootballMatch> factory = new FootballMatchFactory();
        EventType eventType = EventType.GOAL;
        Instant timestamp = Instant.now();

        Team<?> homeTeam = FootballTeam.builder()
                .name("Home")
                .build();
        Team<?> awayTeam = FootballTeam.builder()
                .name("Away")
                .build();

        FootballMatch match = (FootballMatch) factory.createMatchBuilder(homeTeam, awayTeam).build();

        MatchEvent<FootballPlayer> event = new MatchEvent.Builder<FootballPlayer>()
                .eventType(eventType)
                .timestamp(timestamp)
                .involvedPlayers(Collections.emptyList())
                .match(match)
                .build();

        assertEquals("Event type should match the builder input", eventType, event.getEventType());
        assertEquals("Timestamp should match the builder input", timestamp, event.getTimestamp());
        assertTrue("Involved players list should be empty", event.getInvolvedPlayers().isEmpty());
        assertEquals("Match should match the builder input", match, event.getMatch());
    }
}
