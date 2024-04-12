package com.sportradar.exercise.match;

import java.time.Instant;
import java.util.*;

public class MatchEvent<P extends Player> {
    private final EventType eventType;
    private final Instant timestamp;
    private final List<P> involvedPlayers;
    private final MatchInterface match;
    private final Map<String, Object> additionalData;

    private MatchEvent(Builder<P> builder) {
        this.eventType = builder.eventType;
        this.timestamp = builder.timestamp;
        this.involvedPlayers = Collections.unmodifiableList(builder.involvedPlayers);
        this.match = builder.match;
        this.additionalData = builder.additionalData;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public List<P> getInvolvedPlayers() {
        return involvedPlayers;
    }

    public MatchInterface getMatch() {
        return match;
    }

    public Object getAdditionalData(String key) {
        return this.additionalData.get(key);
    }

    public static class Builder<P extends Player> {
        private EventType eventType;
        private Instant timestamp;
        private List<P> involvedPlayers;
        private MatchInterface match;
        private Map<String, Object> additionalData = new HashMap<>();

        public Builder<P> eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder<P> timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder<P> involvedPlayers(List<P> involvedPlayers) {
            this.involvedPlayers = new ArrayList<>(involvedPlayers);
            return this;
        }

        public Builder<P> match(MatchInterface match) {
            this.match = match;
            return this;
        }

        public Builder<P> additionalData(String key, Object value) {
            this.additionalData.put(key, value);
            return this;
        }

        public MatchEvent<P> build() {
            return new MatchEvent<>(this);
        }
    }
}
