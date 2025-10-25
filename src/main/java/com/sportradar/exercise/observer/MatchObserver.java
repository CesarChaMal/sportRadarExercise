package com.sportradar.exercise.observer;



import java.util.logging.Logger;

import static java.lang.System.out;

public class MatchObserver implements Observer<MatchChangeEvent> {
    private boolean eventReceived = false;
    private static final Logger logger = Logger.getLogger(MatchObserver.class.getName());

    @Override
    public void update(MatchChangeEvent event) {
//        logger.info("MatchChangeEvent received for match: " + event.getMatch());
        logger.info("MatchChangeEvent received for match: " + event.match());
        logger.info("Event details: " + event.getDetails());
        eventReceived = true;
    }

    public boolean isEventReceived() {
        return eventReceived;
    }
}
