package com.sportradar.exercise.observer;

import com.sportradar.exercise.match.MatchInterface;

public class MatchChangeEvent {
    private final MatchInterface match;

    public MatchChangeEvent(MatchInterface match) {
        this.match = match;
    }

    public MatchInterface getMatch() {
        return match;
    }
}
