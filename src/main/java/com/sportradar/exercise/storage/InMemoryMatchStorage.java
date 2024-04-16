package com.sportradar.exercise.storage;

import com.sportradar.exercise.match.MatchInterface;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//public class InMemoryMatchStorage implements MatchStorage {
public class InMemoryMatchStorage<T extends MatchInterface> implements MatchStorage<T> {
//    private final Map<Long, MatchInterface> matches = new HashMap<>();
    private final ConcurrentHashMap<Long, T> matches = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void addMatch(T match) {
        rwLock.writeLock().lock();
        try {
            matches.put(match.getId(), match);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void removeMatch(T match) {
        rwLock.writeLock().lock();
        try {
            matches.remove(match.getId());
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public T getMatchById(Long matchId) {
        return matches.get(matchId);
    }

    public List<T> getAllMatches() {
        rwLock.readLock().lock();
        try {
//        return Collections.unmodifiableList(matches.values());
            return new ArrayList<>(matches.values());
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        matches.clear();
    }
}
