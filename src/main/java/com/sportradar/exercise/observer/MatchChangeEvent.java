package com.sportradar.exercise.observer;

import com.sportradar.exercise.match.EventType;
import com.sportradar.exercise.match.MatchInterface;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/*
public class MatchChangeEvent implements EventDetails {
    private final MatchInterface match;
    private final EventType eventType;

    public MatchChangeEvent(MatchInterface match, EventType eventType) {
        this.match = match;
        this.eventType = eventType;
    }

    public MatchInterface getMatch() {
        return match;
    }

    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String getDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        String formattedStartTime = formatter.format(Instant.ofEpochMilli(System.currentTimeMillis()));

        return "MatchEvent: " + eventType + " for Match : " + match.toString() + " at " + formattedStartTime;
    }
}
*/

public record MatchChangeEvent(MatchInterface match, EventType eventType) implements EventDetails {

    @Override
    public String getDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        String formattedStartTime = formatter.format(Instant.ofEpochMilli(System.currentTimeMillis()));

        return "MatchEvent: " + eventType + " for Match : " + match.toString() + " at " + formattedStartTime;
    }
}