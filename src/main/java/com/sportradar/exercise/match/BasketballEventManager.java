package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;
import com.sportradar.exercise.observer.Observer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasketballEventManager implements EventManager {
    private final List<MatchEvent<?>> events = new ArrayList<>();
    private final MatchEventNotifier<MatchChangeEvent> matchEventNotifier;
    private final Match match;

    public BasketballEventManager(Match match) {
        this.match = match;
        this.matchEventNotifier = new MatchEventNotifier<>();
    }

    public void addPointsScoredEvent(BasketballPlayer scorer, int points) {
        List<BasketballPlayer> involvedPlayers = Collections.singletonList(scorer);
        addEvent(EventType.POINTS_SCORED, involvedPlayers, points);
    }

    public void addFoulEvent(BasketballPlayer player) {
        addEvent(EventType.FOUL, Collections.singletonList(player));
    }

    public void addTimeoutEvent(Team<?> team) {
        addEvent(EventType.TIMEOUT, Collections.emptyList());
    }

    public void addSubstitutionEvent(BasketballPlayer playerOut, BasketballPlayer playerIn) {
        List<BasketballPlayer> involvedPlayers = new ArrayList<>();
        involvedPlayers.add(playerOut);
        involvedPlayers.add(playerIn);
        addEvent(EventType.SUBSTITUTION, involvedPlayers);
    }

    @Override
    public void addEvent(MatchEvent<?> event) {
        events.add(event);
//        notifyObservers();
    }

    public void addEvent(EventType eventType, List<? extends Player> involvedPlayers) {
        MatchEvent<?> event = new MatchEvent.Builder<Player>()
                .eventType(eventType)
                .timestamp(Instant.now())
                .involvedPlayers(new ArrayList<>(involvedPlayers))
                .match(match)
                .build();
        events.add(event);
//        notifyObservers();
    }

    public void addEvent(EventType eventType, List<? extends Player> involvedPlayers, int points) {
        MatchEvent<?> event = new MatchEvent.Builder<Player>()
                .eventType(eventType)
                .timestamp(Instant.now())
                .involvedPlayers(new ArrayList<>(involvedPlayers))
                .additionalData("points", points)
                .match(match)
                .build();
        events.add(event);
//        notifyObservers();
    }

    public void addScoreUpdateEvent() {
        addEvent(EventType.SCORE_UPDATE, Collections.emptyList());
    }

    public List<MatchEvent<?>> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public void registerObserver(Observer<MatchChangeEvent> observer) {
        matchEventNotifier.registerObserver(observer);
    }

    public void removeObserver(Observer<MatchChangeEvent> observer) {
        matchEventNotifier.removeObserver(observer);
    }

    private void notifyObservers() {
        matchEventNotifier.notifyObservers(new MatchChangeEvent(match));
    }
}
