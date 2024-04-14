package com.sportradar.exercise.match;

import java.util.List;

public interface EventManager extends StateManagement, ObserverManagement  {
    void addEvent(EventType eventType);
    void addEvent(EventType eventType, List<? extends Player> involvedPlayers);
    void addScoreUpdateEvent();
    List<MatchEvent<?>> getEvents();
}
