package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;
import com.sportradar.exercise.observer.Observer;

import java.util.List;

public interface EventManager extends StateManagement, ObserverManagement  {
    void addEvent(EventType eventType);
    void addEvent(EventType eventType, List<? extends Player> involvedPlayers);
    void addScoreUpdateEvent();
    List<MatchEvent<?>> getEvents();
}
