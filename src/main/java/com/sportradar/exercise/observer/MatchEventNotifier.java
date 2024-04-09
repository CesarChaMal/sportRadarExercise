package com.sportradar.exercise.observer;

import java.util.HashSet;
import java.util.Set;

public class MatchEventNotifier<T> implements Subject<T> {
    private Set<Observer<T>> observers = new HashSet<>();

    @Override
    public void registerObserver(Observer<T> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<T> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(T event) {
        for (Observer<T> observer : observers) {
            observer.update(event);
        }
    }
}
