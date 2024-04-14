package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;
import com.sportradar.exercise.observer.Observer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasketballEventManager implements EventManager {
//    private final List<MatchEvent<?>> events = new ArrayList<>();
    private final MatchEventNotifier<MatchChangeEvent> matchEventNotifier;
    private final Match match;
    private final MatchStateController stateController;
    private CommonEventManager commonEventManager;

    public BasketballEventManager(Match match) {
        this.match = match;
        this.matchEventNotifier = new MatchEventNotifier<>(MatchChangeEvent.class);
        this.stateController = new MatchStateController(match, matchEventNotifier);
        this.commonEventManager = new CommonEventManager(match, matchEventNotifier);
    }

    public void addPointsScoredEvent(BasketballPlayer scorer, int points) {
        Team<?> scoringTeam = scorer.getTeam();
//        List<BasketballPlayer> involvedPlayers = Collections.singletonList(scorer);
        List<BasketballPlayer> involvedPlayers = List.of(scorer);
        EventType eventType = EventType.determineEventTypeByPoints(points);
        addEvent(eventType, involvedPlayers, points);
        match.incrementScore(EventType.POINTS_SCORED, scoringTeam, points);
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

    public void addEvent(EventType eventType) {
        commonEventManager.addEvent(eventType);
    }

    public void addEvent(EventType eventType, List<? extends Player> involvedPlayers) {
        commonEventManager.addEvent(eventType, involvedPlayers);
    }

    public void addEvent(EventType eventType, List<? extends Player> involvedPlayers, int points) {
        MatchEvent<?> event = new MatchEvent.Builder<Player>()
                .eventType(eventType)
                .timestamp(Instant.now())
                .involvedPlayers(new ArrayList<>(involvedPlayers))
                .additionalData("points", points)
                .match(match)
                .build();
        commonEventManager.addEventToList(event);
    }

    public void addScoreUpdateEvent() {
        commonEventManager.addScoreUpdateEvent();
    }

    public List<MatchEvent<?>> getEvents() {
        return commonEventManager.getEvents();
    }

    public void registerObserver(Observer<MatchChangeEvent> observer) {
//        matchEventNotifier.registerObserver(observer);
        commonEventManager.registerObserver(observer);
    }

    public void removeObserver(Observer<MatchChangeEvent> observer) {
//        matchEventNotifier.removeObserver(observer);
        commonEventManager.removeObserver(observer);
    }

    public void notifyObservers(MatchChangeEvent matchChangeEvent) {
//        matchEventNotifier.notifyObservers(matchChangeEvent);
        commonEventManager.notifyObservers(matchChangeEvent);
    }

    @Override
    public MatchEventNotifier<MatchChangeEvent> getMatchEventNotifier() {
        return matchEventNotifier;
    }

/*
    @Override
    public void setMatchEventNotifier(MatchEventNotifier<MatchChangeEvent>  notifier) {
        this.matchEventNotifier = notifier;
    }
*/

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
