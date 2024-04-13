package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;
import com.sportradar.exercise.observer.Observer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FootballEventManager implements EventManager {
//    private final List<MatchEvent<?>> events = new ArrayList<>();
    private MatchEventNotifier<MatchChangeEvent> matchEventNotifier;
    private final Match match;
    private final MatchStateController stateController;
    private CommonEventManager commonEventManager;

    public FootballEventManager(Match match) {
        this.match = match;
        this.matchEventNotifier = new MatchEventNotifier<>(MatchChangeEvent.class);
        this.stateController = new MatchStateController(match, matchEventNotifier);
        this.commonEventManager = new CommonEventManager(match, matchEventNotifier);
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
        addEvent(EventType.MATCH_STARTED);
    }

    public void addMatchEndEvent() {
        addEvent(EventType.MATCH_FINISHED, Collections.emptyList());
    }

    public void addEvent(EventType eventType) {
        commonEventManager.addEvent(eventType);
    }

    public void addEvent(EventType eventType, List<? extends Player> involvedPlayers) {
        commonEventManager.addEvent(eventType, involvedPlayers);
    }

    public void addScoreUpdateEvent() {
        commonEventManager.addScoreUpdateEvent();
    }

    public List<MatchEvent<?>> getEvents() {
        return commonEventManager.getEvents();
    }

    public void registerObserver(Observer<MatchChangeEvent> observer) {
        matchEventNotifier.registerObserver(observer);
    }

    public void removeObserver(Observer<MatchChangeEvent> observer) {
        matchEventNotifier.removeObserver(observer);
    }

    @Override
    public void notifyObservers(MatchChangeEvent matchChangeEvent) {
        matchEventNotifier.notifyObservers(matchChangeEvent);
    }

    @Override
    public MatchEventNotifier<MatchChangeEvent> getMatchEventNotifier() {
        return matchEventNotifier;
    }

    @Override
    public void setMatchEventNotifier(MatchEventNotifier<MatchChangeEvent>  notifier) {
        this.matchEventNotifier = notifier;
    }

    @Override
    public void startMatch() {
        addEvent(EventType.MATCH_STARTED);
        stateController.startMatch();
    }

    @Override
    public void finishMatch() {
        addEvent(EventType.MATCH_FINISHED);
        stateController.finishMatch();
    }

    @Override
    public void pauseMatch() {
        addEvent(EventType.MATCH_PAUSED);
        stateController.pauseMatch();
    }

    @Override
    public void resumeMatch() {
        addEvent(EventType.MATCH_RESUMED);
        stateController.resumeMatch();
    }
}
