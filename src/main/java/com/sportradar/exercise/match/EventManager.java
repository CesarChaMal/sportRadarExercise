package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.observer.Observer;

import java.util.List;

public interface EventManager {
    void addEvent(MatchEvent<?> event);
    void addEvent(EventType eventType, List<? extends Player> involvedPlayers);
    void addScoreUpdateEvent();
    List<MatchEvent<?>> getEvents();
    void registerObserver(Observer<MatchChangeEvent> observer);
    void removeObserver(Observer<MatchChangeEvent> observer);
}
