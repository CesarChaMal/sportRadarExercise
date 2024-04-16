package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;
import com.sportradar.exercise.observer.Observer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CommonEventManager  {
//    private List<MatchEvent<?>> events = new ArrayList<>();
    private List<MatchEvent<?>> events = new CopyOnWriteArrayList<>();
    private MatchEventNotifier<MatchChangeEvent> matchEventNotifier;
    private Match match;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public CommonEventManager(Match match, MatchEventNotifier<MatchChangeEvent> matchEventNotifier) {
        this.match = match;
        this.matchEventNotifier = matchEventNotifier;
    }

    public void addEvent(EventType eventType) {
        addEvent(eventType, Collections.emptyList());
    }

    public void addEvent(EventType eventType, List<? extends Player> involvedPlayers) {
        lock.writeLock().lock();
        try {
            MatchEvent<?> event = new MatchEvent.Builder<Player>()
                    .eventType(eventType)
                    .timestamp(Instant.now())
                    .involvedPlayers(new ArrayList<>(involvedPlayers))
                    .match(match)
                    .build();
            events.add(event);
        } finally {
            lock.writeLock().unlock(); // Unlock after writing
        }
    }

    public void addScoreUpdateEvent() {
        addEvent(EventType.SCORE_UPDATE, Collections.emptyList());
    }

    public void addEventToList(MatchEvent<?> event) {
        lock.writeLock().lock();
        try {
            events.add(event);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<MatchEvent<?>> getEvents() {
//        return Collections.unmodifiableList(events);
//        return new ArrayList<>(events);
        lock.readLock().lock();
        try {
            return new ArrayList<>(events);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void registerObserver(Observer<MatchChangeEvent> observer) {
        matchEventNotifier.registerObserver(observer);
    }

    public void removeObserver(Observer<MatchChangeEvent> observer) {
        matchEventNotifier.removeObserver(observer);
    }

    public void notifyObservers(MatchChangeEvent matchChangeEvent) {
        matchEventNotifier.notifyObservers(matchChangeEvent);
    }
}
