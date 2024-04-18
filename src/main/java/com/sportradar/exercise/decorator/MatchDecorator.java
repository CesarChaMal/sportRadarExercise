package com.sportradar.exercise.decorator;

import com.sportradar.exercise.match.EventManager;
import com.sportradar.exercise.match.Match;

import java.util.function.Function;

public abstract class MatchDecorator extends Match {
    protected Match match;
    private final EventManager eventManager;

    protected MatchDecorator(Builder<?> builder) {
        super(builder);
        this.match = builder.match;
        this.eventManager = match.getEventManager();
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public void startMatch() {
        match.startMatch();
    }

    @Override
    public void finishMatch() {
        match.finishMatch();
    }

    public abstract static class Builder<T extends Builder<T>> extends Match.Builder<T> {

        protected Match match;

        public Builder(Match match) {
            super(match.getHomeTeam(), match.getAwayTeam());
            this.match = match;
            this.eventManagerFactory = x -> match.getEventManager();
        }

        @Override
        public abstract MatchDecorator build();

        @Override
        protected abstract T self();
    }
}
