package com.sportradar.exercise.observer;

import static java.lang.System.out;

public class MatchObserver implements Observer<MatchChangeEvent> {
    private boolean eventReceived = false;

    @Override
    public void update(MatchChangeEvent event) {
//        out.println("MatchChangeEvent received for match: " + event.getMatch());
        out.println("MatchChangeEvent received for match: " + event.match());
        out.println("Event details: " + event.getDetails());
        eventReceived = true;
    }

    public boolean isEventReceived() {
        return eventReceived;
    }
}
