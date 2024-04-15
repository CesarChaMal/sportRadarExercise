package com.sportradar.exercise.storage;

import com.sportradar.exercise.match.MatchInterface;

import java.util.*;

public class InMemoryMatchStorage implements MatchStorage {
    private final Map<Long, MatchInterface> matches = new HashMap<>();

    @Override
    public void addMatch(MatchInterface match) {
        matches.put(match.getId(), match);
    }

    @Override
    public void removeMatch(MatchInterface match) {
        matches.remove(match.getId());
    }

    @Override
    public MatchInterface getMatchById(Long matchId) {
        return matches.get(matchId);
    }

    @Override
    public List<MatchInterface> getAllMatches() {
//        return Collections.unmodifiableList(matches.values());
        return new ArrayList<>(matches.values());
    }
}
