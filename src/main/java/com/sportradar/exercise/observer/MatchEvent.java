package com.sportradar.exercise.observer;

import com.sportradar.exercise.match.MatchInterface;

/*
public class MatchEvent {
    private final MatchInterface match;

    public MatchEvent(MatchInterface match) {
        this.match = match;
    }

    public MatchInterface getMatch() {
        return match;
    }
}
*/

public record MatchEvent(MatchInterface match) {}
