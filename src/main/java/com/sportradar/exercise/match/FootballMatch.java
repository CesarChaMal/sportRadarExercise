package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;

import java.util.concurrent.locks.ReentrantLock;

public class FootballMatch extends Match {
    private final ReentrantLock lock = new ReentrantLock();

    private FootballMatch(Builder builder) {
        super(builder);
    }

    public void scoreHomeGoal(FootballPlayer scorer, FootballPlayer assistant) {
        lock.lock();
        try {
            incrementHomeScore(1);
            ((FootballEventManager)getEventManager()).addGoalEvent(scorer, assistant);
        } finally {
            lock.unlock();
        }
        notifyObservers(new MatchChangeEvent(this, EventType.GOAL));
    }

    public void scoreAwayGoal(FootballPlayer scorer, FootballPlayer assistant) {
        lock.lock();
        try {
            incrementAwayScore(1);
            ((FootballEventManager)getEventManager()).addGoalEvent(scorer, assistant);
        } finally {
            lock.unlock();
        }
        notifyObservers(new MatchChangeEvent(this, EventType.GOAL));
    }

    public void scoreGoal(FootballPlayer scorer, FootballPlayer assistant) {
        FootballEventManager manager = (FootballEventManager) getEventManager();
        if (manager == null) {
            throw new IllegalStateException("Event manager is not configured for football");
        }
        lock.lock();
        try {
            manager.addGoalEvent(scorer, assistant);
        } finally {
            lock.unlock();
        }
    }

    public static class Builder extends Match.Builder<Builder> {
        public Builder(Team<?> homeTeam, Team<?> awayTeam) {
            super(homeTeam, awayTeam);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public FootballMatch build() {
            if (super.eventManagerFactory == null) {
                super.eventManagerFactory = match -> new FootballEventManager((FootballMatch) match);
            }
            return new FootballMatch(this);
        }
    }
}
