package com.sportradar.exercise.scoring;

import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.analytics.MatchSummaryGenerator;
import com.sportradar.exercise.command.CommandExecutor;
import com.sportradar.exercise.command.FinishMatchCommand;
import com.sportradar.exercise.command.StartMatchCommand;
import com.sportradar.exercise.command.UpdateScoreCommand;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.storage.InMemoryMatchStorage;
import com.sportradar.exercise.storage.MatchStorage;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import static java.lang.System.*;
import static java.lang.System.out;


public class Scoreboard implements MatchStorage<MatchInterface> {
    private static Scoreboard instance;
    private static final Lock lock = new ReentrantLock();
//    private final List<MatchInterface> matches;
    private final CommandExecutor commandExecutor;
    private final MatchSummaryGenerator summaryGenerator;
    private final MatchFactory matchFactory;
    private final MatchStorage<MatchInterface> matchStorage;
    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private Semaphore matchStartSemaphore = new Semaphore(5);
    private static final Logger logger = Logger.getLogger(Scoreboard.class.getName());

    Scoreboard(MatchFactory matchFactory) {
//        matches = new ArrayList<>();
        this.commandExecutor = new CommandExecutor(this);
        this.summaryGenerator = new MatchSummaryGenerator();
        this.matchFactory = matchFactory;
        this.matchStorage = new InMemoryMatchStorage();
        this.executorService = Executors.newFixedThreadPool(4);
    }

    private Scoreboard(MatchFactory matchFactory, CommandExecutor commandExecutor, MatchSummaryGenerator summaryGenerator, MatchStorage<MatchInterface> matchStorage) {
//        this.matches = new ArrayList<>();
        this.commandExecutor = commandExecutor;
        this.summaryGenerator = summaryGenerator;
        this.matchFactory = matchFactory;
        this.matchStorage = matchStorage;
        this.executorService = Executors.newFixedThreadPool(4);
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

    public static Scoreboard getInstance(MatchFactory matchFactory, CommandExecutor commandExecutor, MatchSummaryGenerator summaryGenerator, MatchStorage<MatchInterface> matchStorage) {
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

    public MatchFactory getMatchFactory() {
        return matchFactory;
    }

    public boolean startMatch(Team<?> homeTeam, Team<?> awayTeam) {
        logger.info("Trying to start match between " + homeTeam.getName() + " and " + awayTeam.getName());
        if (!matchStartSemaphore.tryAcquire()) {
            logger.info("Failed to acquire semaphore for match between " + homeTeam.getName() + " and " + awayTeam.getName());
            return false;
        }
//        commandExecutor.executeCommand(startMatchCommand);
//        Future<?> future = executorService.submit(() -> startMatchCommand.execute());
        try {
            logger.info("Semaphore acquired for match start");
            StartMatchCommand startMatchCommand = new StartMatchCommand(this, this.matchFactory, homeTeam, awayTeam);
            Future<Boolean> future = executorService.submit(() -> {
                startMatchCommand.execute();
                return true;
            });
            handleFutureCompletion(future);
            return true;
        } catch (Exception e) {
            logger.severe("Error starting match: " + e.getMessage());
            matchStartSemaphore.release();
            return false;
        }
    }

    public synchronized void updateScore(MatchInterface match, int homeScore, int awayScore) {
        logger.info("Updating score for match ID " + match.getId());
        UpdateScoreCommand updateScoreCommand = new UpdateScoreCommand(match, homeScore, awayScore);
//        commandExecutor.executeCommand(updateScoreCommand);
//        Future<?> future = executorService.submit(() -> updateScoreCommand.execute());
        Future<Boolean> future = executorService.submit(() -> {
            updateScoreCommand.execute();
            return true;
        });
        handleFutureCompletion(future);
        logger.info("Scores updated for match: " + match.getId() + " - New Home Score: " + match.getHomeScore() + ", New Away Score: " + match.getAwayScore());
    }

    public void finishMatch(MatchInterface match) {
        logger.info("Finishing match with ID " + match.getId());
        FinishMatchCommand finishMatchCommand = new FinishMatchCommand(this, match);
//        commandExecutor.executeCommand(finishMatchCommand);
//        Future<?> future = executorService.submit(() -> finishMatchCommand.execute());
        try {
            Future<Boolean> future = executorService.submit(() -> {
                finishMatchCommand.execute();
                return true;
            });
            handleFutureCompletion(future);
        } catch (Exception e) {
            logger.severe("Error finishing match: " + e.getMessage());
        } finally {
            matchStartSemaphore.release();
            logger.info("Semaphore released after finishing match with ID " + match.getId());
        }
    }

    public void finishAllMatches() {
        logger.info("Finishing all matches and releasing resources.");
        getAllMatches().forEach(match -> {
            try {
                finishMatch(match);
            } catch (Exception e) {
                logger.severe("Error finishing match with ID: " + match.getId() + " - " + e.getMessage());
            }
        });
    }

    private void handleFutureCompletion(Future<Boolean> future) {
        try {
            Boolean result = future.get(500, TimeUnit.MILLISECONDS);
            logger.info("Future completed with result: " + result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            err.println("Task was interrupted.");
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            err.println("An error occurred during task execution: " + cause.getMessage());
            cause.printStackTrace();
        } catch (TimeoutException e) {
            err.println("Future did not complete in time.");
        }
    }

    public boolean tryStartMatch() {
        return matchStartSemaphore.tryAcquire();
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
        return summaryGenerator.generateSummary(this.getAllMatches());
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

    public void shutdown() {
        resourceCleanup();
    }

    public void clear() {
        matchStorage.clear();
        matchStartSemaphore = new Semaphore(5);
    }

    private void resourceCleanup() {
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        executorService = Executors.newFixedThreadPool(4);
    }
}
