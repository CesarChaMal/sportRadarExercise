package com.sportradar.exercise.scoring;

import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.analytics.MatchSummaryGenerator;
import com.sportradar.exercise.command.CommandExecutor;
import com.sportradar.exercise.command.FinishMatchCommand;
import com.sportradar.exercise.command.StartMatchCommand;
import com.sportradar.exercise.command.UpdateScoreCommand;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.storage.InMemoryMatchStorage;
import com.sportradar.exercise.storage.MatchStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Scoreboard implements  MatchStorage {
    private static Scoreboard instance;
    private static final Lock lock = new ReentrantLock();
//    private final List<MatchInterface> matches;
    private final CommandExecutor commandExecutor;
    private final MatchSummaryGenerator summaryGenerator;
    private final MatchFactory matchFactory;
    private final MatchStorage matchStorage;

    Scoreboard(MatchFactory matchFactory) {
//        matches = new ArrayList<>();
        this.commandExecutor = new CommandExecutor(this);
        this.summaryGenerator = new MatchSummaryGenerator();
        this.matchFactory = matchFactory;
        this.matchStorage = new InMemoryMatchStorage();
    }

    private Scoreboard(MatchFactory matchFactory, CommandExecutor commandExecutor, MatchSummaryGenerator summaryGenerator, MatchStorage matchStorage) {
//        this.matches = new ArrayList<>();
        this.commandExecutor = commandExecutor;
        this.summaryGenerator = summaryGenerator;
        this.matchFactory = matchFactory;
        this.matchStorage = matchStorage;
    }

    public static synchronized Scoreboard getInstance(MatchFactory matchFactory) {
        lock.lock();
        try {
            if (instance == null) {
                instance = new Scoreboard(matchFactory);
            }
        } finally {
            lock.unlock();
        }
        return instance;
    }

    public static Scoreboard getInstance(MatchFactory matchFactory, CommandExecutor commandExecutor, MatchSummaryGenerator summaryGenerator, MatchStorage matchStorage) {
        lock.lock();
        try {
            if (instance == null) {
                instance = new Scoreboard(matchFactory, commandExecutor, summaryGenerator, matchStorage);
            }
        } finally {
            lock.unlock();
        }
        return instance;
    }

    public void startMatch(Team<?> homeTeam, Team<?> awayTeam) {
        StartMatchCommand startMatchCommand = new StartMatchCommand(this, this.matchFactory, homeTeam, awayTeam);
        commandExecutor.executeCommand(startMatchCommand);
    }

    public void updateScore(MatchInterface match, int homeScore, int awayScore) {
        UpdateScoreCommand updateScoreCommand = new UpdateScoreCommand(match, homeScore, awayScore);
        commandExecutor.executeCommand(updateScoreCommand);
    }

    public void finishMatch(MatchInterface match) {
        FinishMatchCommand finishMatchCommand = new FinishMatchCommand(this, match);
        commandExecutor.executeCommand(finishMatchCommand);
    }

    public void addMatch(MatchInterface match) {
//        matches.add(match);
        this.matchStorage.addMatch(match);
    }

    public void removeMatch(MatchInterface match) {
//        matches.remove(match);
        this.matchStorage.removeMatch(match);
    }

    @Override
    public MatchInterface getMatchById(Long matchId) {
        return this.matchStorage.getMatchById(matchId);
    }

    public List<MatchInterface> getAllMatches() {
//        return Collections.unmodifiableList(matches);
//        return new ArrayList<>(matches);
        return this.matchStorage.getAllMatches();
    }

    //O(n log n
    public List<MatchInterface> getSummary() {
//        return summaryGenerator.generateSummary(new ArrayList<>(matches));
        return summaryGenerator.generateSummary(new ArrayList<>(this.getAllMatches()));
    }

    public Optional<MatchInterface> getMatch(Team<?> homeTeam, Team<?> awayTeam) {
/*
        return matches.stream()
                .filter(match -> match.getHomeTeam().equals(homeTeam) && match.getAwayTeam().equals(awayTeam))
                .findFirst();
*/
        return getAllMatches().stream()
                .filter(match -> match.getHomeTeam().equals(homeTeam) && match.getAwayTeam().equals(awayTeam))
                .findFirst();
    }
}
