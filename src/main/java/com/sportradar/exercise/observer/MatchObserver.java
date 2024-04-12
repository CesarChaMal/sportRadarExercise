package com.sportradar.exercise.observer;

public class MatchObserver implements Observer<MatchChangeEvent> {
    private boolean eventReceived = false;

    @Override
    public void update(MatchChangeEvent event) {
        System.out.println("MatchChangeEvent received for match: " + event.getMatch());
        eventReceived = true;
    }

    public boolean isEventReceived() {
        return eventReceived;
    }
}
