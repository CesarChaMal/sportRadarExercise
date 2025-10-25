package com.sportradar.exercise.service;

import com.sportradar.exercise.analytics.MatchSummaryGenerator;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.storage.InMemoryMatchStorage;
import com.sportradar.exercise.storage.MatchStorage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreboardService {
    private final MatchSummaryGenerator summaryGenerator;
    private final MatchStorage<MatchInterface> matchStorage;

    public ScoreboardService() {
        this.summaryGenerator = new MatchSummaryGenerator();
        this.matchStorage = new InMemoryMatchStorage();
    }



    public void addMatch(MatchInterface match) {
        this.matchStorage.addMatch(match);
    }

    public void removeMatch(MatchInterface match) {
        this.matchStorage.removeMatch(match);
    }

    public List<MatchInterface> getSummary() {
        return summaryGenerator.generateSummary(this.getAllMatches());
    }

    public List<MatchInterface> getAllMatches() {
        return this.matchStorage.getAllMatches();
    }

    public void clear() {
        matchStorage.clear();
    }
}