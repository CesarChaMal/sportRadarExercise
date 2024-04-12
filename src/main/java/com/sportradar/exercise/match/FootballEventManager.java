package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;
import com.sportradar.exercise.observer.Observer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FootballEventManager implements EventManager {
    private final List<MatchEvent<?>> events = new ArrayList<>();
    private final MatchEventNotifier<MatchChangeEvent> matchEventNotifier;
    private final Match match;

    public FootballEventManager(Match match) {
        this.match = match;
        this.matchEventNotifier = new MatchEventNotifier<>();
    }

    public void addGoalEvent(FootballPlayer scorer, FootballPlayer assistant) {
        List<FootballPlayer> involvedPlayers = new ArrayList<>();
        involvedPlayers.add(scorer);
        if (assistant != null) involvedPlayers.add(assistant);
        addEvent(EventType.GOAL, involvedPlayers);
    }

    public void addCardEvent(EventType cardType, FootballPlayer player) {
        if (cardType != EventType.YELLOW_CARD && cardType != EventType.RED_CARD) {
            throw new IllegalArgumentException("Invalid card type for this method");
        }
        addEvent(cardType, Collections.singletonList(player));
    }

    public void addSubstitutionEvent(FootballPlayer playerOut, FootballPlayer playerIn) {
        List<FootballPlayer> involvedPlayers = new ArrayList<>();
        involvedPlayers.add(playerOut);
        involvedPlayers.add(playerIn);
        addEvent(EventType.SUBSTITUTION, involvedPlayers);
    }

    public void addPenaltyEvent(FootballPlayer player) {
        addEvent(EventType.PENALTY, Collections.singletonList(player));
    }

    public void addMatchStartEvent() {
        addEvent(EventType.MATCH_STARTED, Collections.emptyList());
    }

    public void addMatchEndEvent() {
        addEvent(EventType.MATCH_FINISHED, Collections.emptyList());
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
