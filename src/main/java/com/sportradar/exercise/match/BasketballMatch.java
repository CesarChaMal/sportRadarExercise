package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;

import java.util.List;

public class BasketballMatch extends Match {

    private BasketballMatch(Builder builder) {
        super(builder);
    }

    public void scorePoints(BasketballPlayer scorer, int points) {
        BasketballEventManager manager = (BasketballEventManager) getEventManager();
        if (manager == null) {
            throw new IllegalStateException("Event manager is not configured for basketball");
        }
        manager.addPointsScoredEvent(scorer, points);
    }

    public void scoreHomePoints(BasketballPlayer scorer, int points) {
        if (scorer.getTeam().equals(this.getHomeTeam())) {
            incrementHomeScore(points);
        } else {
            throw new IllegalArgumentException("Scorer is not part of the home team");
        }
        EventType eventType = EventType.determineEventTypeByPoints(points);
        notifyObservers(new MatchChangeEvent(this, eventType));
    }

    public void scoreAwayPoints(BasketballPlayer scorer, int points) {
        if (scorer.getTeam().equals(this.getAwayTeam())) {
            incrementAwayScore(points);
        } else {
            throw new IllegalArgumentException("Scorer is not part of the away team");
        }
        EventType eventType = EventType.determineEventTypeByPoints(points);
        notifyObservers(new MatchChangeEvent(this, eventType));
    }

    public void addEvent(EventType eventType, List<? extends Player> involvedPlayers, int points) {
        BasketballEventManager manager = (BasketballEventManager) getEventManager();
        if (manager == null) {
            throw new IllegalStateException("Event manager is not configured for basketball");
        }
        manager.addEvent(eventType, involvedPlayers, points);
    }

    public static class Builder extends Match.Builder<BasketballMatch.Builder> {
        public Builder(Team<?> homeTeam, Team<?> awayTeam) {
            super(homeTeam, awayTeam);
        }
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public BasketballMatch build() {
            return new BasketballMatch(this);
        }
    }
}
