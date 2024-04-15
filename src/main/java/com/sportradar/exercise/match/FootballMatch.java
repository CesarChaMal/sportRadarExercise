package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;

public class FootballMatch extends Match {
    private FootballMatch(Builder builder) {
        super(builder);
    }

    public void scoreHomeGoal(FootballPlayer scorer, FootballPlayer assistant) {
        incrementHomeScore(1);
        FootballEventManager manager = (FootballEventManager) getEventManager();
        manager.addGoalEvent(scorer, assistant);
        notifyObservers(new MatchChangeEvent(this, EventType.GOAL));
    }

    public void scoreAwayGoal(FootballPlayer scorer, FootballPlayer assistant) {
        incrementAwayScore(1);
        ((FootballEventManager)getEventManager()).addGoalEvent(scorer, assistant);
        notifyObservers(new MatchChangeEvent(this, EventType.GOAL));
    }

    public void scoreGoal(FootballPlayer scorer, FootballPlayer assistant) {
        FootballEventManager manager = (FootballEventManager) getEventManager();
        if (manager == null) {
            throw new IllegalStateException("Event manager is not configured for footbal");
        }
        manager.addGoalEvent(scorer, assistant);
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
