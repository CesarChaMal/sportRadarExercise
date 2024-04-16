package com.sportradar.exercise.storage;

import com.sportradar.exercise.match.MatchInterface;

import java.util.List;

public interface MatchStorage<T extends MatchInterface> {
    void addMatch(T match);
    void removeMatch(T match);
    T getMatchById(Long matchId);
    List<T> getAllMatches();
    void clear();
}
