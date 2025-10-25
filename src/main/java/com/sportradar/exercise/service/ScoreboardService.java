package com.sportradar.exercise.service;

import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.analytics.MatchSummaryGenerator;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.storage.InMemoryMatchStorage;
import com.sportradar.exercise.storage.MatchStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

@Service
public class ScoreboardService {
    private static final Logger logger = Logger.getLogger(ScoreboardService.class.getName());
    
    private final MatchSummaryGenerator summaryGenerator;
    private final MatchFactory matchFactory;
    private final MatchStorage<MatchInterface> matchStorage;
    private final Semaphore matchStartSemaphore;
    
    @Value("${scoreboard.max-concurrent-matches:5}")
    private int maxConcurrentMatches;

    public ScoreboardService(MatchFactory matchFactory) {
        this.summaryGenerator = new MatchSummaryGenerator();
        this.matchFactory = matchFactory;
        this.matchStorage = new InMemoryMatchStorage();
        this.matchStartSemaphore = new Semaphore(maxConcurrentMatches);
    }

    @Async
    public CompletableFuture<Boolean> startMatchAsync(Team<?> homeTeam, Team<?> awayTeam) {
        logger.info("Starting match between " + homeTeam.getName() + " and " + awayTeam.getName());
        
        MatchInterface match = matchFactory.createMatchBuilder(homeTeam, awayTeam).build();
        addMatch(match);
        match.startMatch();
        
        return CompletableFuture.completedFuture(true);
    }

    public void updateScore(MatchInterface match, int homeScore, int awayScore) {
        logger.info("Updating score for match ID " + match.getId());
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
    }

    public void finishMatch(MatchInterface match) {
        logger.info("Finishing match with ID " + match.getId());
        match.finishMatch();
        removeMatch(match);
    }

    public void addMatch(MatchInterface match) {
        this.matchStorage.addMatch(match);
    }

    public void removeMatch(MatchInterface match) {
        this.matchStorage.removeMatch(match);
    }

    public MatchInterface getMatchById(Long matchId) {
        return this.matchStorage.getMatchById(matchId);
    }

    public List<MatchInterface> getAllMatches() {
        return this.matchStorage.getAllMatches();
    }

    public List<MatchInterface> getSummary() {
        return summaryGenerator.generateSummary(this.getAllMatches());
    }

    public Optional<MatchInterface> getMatch(Team<?> homeTeam, Team<?> awayTeam) {
        return getAllMatches().stream()
                .filter(match -> match.getHomeTeam().equals(homeTeam) && match.getAwayTeam().equals(awayTeam))
                .findFirst();
    }

    public void clear() {
        matchStorage.clear();
    }
}