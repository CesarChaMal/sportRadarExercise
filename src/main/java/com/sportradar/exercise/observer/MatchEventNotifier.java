package com.sportradar.exercise.observer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

public class MatchEventNotifier<T extends EventDetails> implements Subject<T> {
//    private Set<Observer<T>> observers = new HashSet<>();
    private final Set<Observer<T>> observers = Collections.newSetFromMap(new ConcurrentHashMap<Observer<T>, Boolean>());
//    private Set<Observer<T>> observers = new HashSet<>();
    private Class<T> eventType;

    public MatchEventNotifier(Class<T> eventType) {
        this.eventType = eventType;
    }

    @Override
    public void registerObserver(Observer<T> observer) {
        observers.add(observer);
        debugLog("Registered an observer. Total observers: " + observers.size());
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
        debugLog("Removed an observer. Remaining observers: " + observers.size());
    }

    @Override
    public void notifyObservers(T event) {
        if (observers.isEmpty()) {
            debugLog("No observers to notify for event: " + event.getDetails());
        } else {
            debugLog("Notifying " + observers.size() + " observers about event: " + event.getDetails());
            observers.forEach(observer -> observer.update(event));
        }
    }

    public Class<T> getEventType() {
        return eventType;
    }

    public Set<Observer<T>> getObservers() {
        return Collections.unmodifiableSet(observers);
    }

    private void debugLog(String message) {
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(formattedTime + " - " + message);
    }
}
