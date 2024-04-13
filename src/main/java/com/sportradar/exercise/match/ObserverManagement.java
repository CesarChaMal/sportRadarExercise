package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;
import com.sportradar.exercise.observer.Observer;

public interface ObserverManagement {
    void registerObserver(Observer<MatchChangeEvent> observer);
    void removeObserver(Observer<MatchChangeEvent> observer);
    void notifyObservers(MatchChangeEvent matchChangeEvent);
    MatchEventNotifier<MatchChangeEvent> getMatchEventNotifier();
    void setMatchEventNotifier(MatchEventNotifier<MatchChangeEvent>  notifier);
}
