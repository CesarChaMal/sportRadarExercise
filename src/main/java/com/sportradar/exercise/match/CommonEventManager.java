package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonEventManager {
    private List<MatchEvent<?>> events = new ArrayList<>();
    private MatchEventNotifier<MatchChangeEvent> matchEventNotifier;
    private Match match;

    public CommonEventManager(Match match, MatchEventNotifier<MatchChangeEvent> matchEventNotifier) {
        this.match = match;
        this.matchEventNotifier = matchEventNotifier;
    }

    public void addEvent(EventType eventType) {
        addEvent(eventType, Collections.emptyList());
    }

    public void addEvent(EventType eventType, List<? extends Player> involvedPlayers) {
        MatchEvent<?> event = new MatchEvent.Builder<Player>()
                .eventType(eventType)
                .timestamp(Instant.now())
                .involvedPlayers(new ArrayList<>(involvedPlayers))
                .match(match)
                .build();
        events.add(event);
    }

    public void addScoreUpdateEvent() {
        addEvent(EventType.SCORE_UPDATE, Collections.emptyList());
    }

    public void addEventToList(MatchEvent<?> event) {
        events.add(event);
    }

    public List<MatchEvent<?>> getEvents() {
//        return Collections.unmodifiableList(events);
        return new ArrayList<>(events);
    }
}
