package com.sportradar.exercise.storage;

import com.sportradar.exercise.match.MatchInterface;

import java.util.List;

public interface MatchStorage {
    void addMatch(MatchInterface match);
    void removeMatch(MatchInterface match);
    MatchInterface getMatchById(Long matchId);
    List<MatchInterface> getAllMatches();
}
