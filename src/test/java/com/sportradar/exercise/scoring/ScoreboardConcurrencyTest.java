package com.sportradar.exercise.scoring;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.match.FootballTeam;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.match.StateManagement;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.state.MatchState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ScoreboardConcurrencyTest {
    private FootballMatchFactory matchFactory;
    private Scoreboard scoreboard;
    private ExecutorService executorService;
    private static final Logger logger = Logger.getLogger(ScoreboardConcurrencyTest.class.getName());

    @Before
    public void setUp() {
        matchFactory = new FootballMatchFactory();
        scoreboard = Scoreboard.getInstance(matchFactory);
        scoreboard.clear();
        executorService = Executors.newFixedThreadPool(4);
    }

    @After
    public void tearDown() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        scoreboard.finishAllMatches();
    }

    @Test
    public void testConcurrentScoreSetting() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(10);

        FootballTeam homeTeam = FootballTeam.builder().name("Home").build();
        FootballTeam awayTeam = FootballTeam.builder().name("Away").build();

        scoreboard.startMatch(homeTeam, awayTeam);
        MatchInterface match = scoreboard.getMatch(homeTeam, awayTeam)
                .orElseThrow(() -> new AssertionError("Match not found"));

        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            service.execute(() -> {
                int count = counter.incrementAndGet();
                scoreboard.updateScore(match, count, count);
                latch.countDown();
            });
        }

        latch.await();
        service.shutdown();

        assertTrue("Unexpected score values",
match.getHomeScore() >= 1 && match.getHomeScore() <= 10 &&
                        match.getHomeScore() == match.getAwayScore());
    }

    @Test
    public void testMatchStartSemaphore() throws InterruptedException {
        final int threadCount = 4;
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger activeStarts = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            service.execute(() -> {
                try {
                    barrier.await();
                    if (scoreboard.tryStartMatch()) {
                        FootballTeam homeTeam = FootballTeam.builder().name("Home" + Thread.currentThread().getId()).build();
                        FootballTeam awayTeam = FootballTeam.builder().name("Away" + Thread.currentThread().getId()).build();
                        scoreboard.startMatch(homeTeam, awayTeam);
                        activeStarts.incrementAndGet();
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        endLatch.await();
        service.shutdown();

        assertEquals("Semaphore did not properly limit match starts", 4, activeStarts.get());
    }

    @Test
    public void testConcurrentUpdatesAndSummaryOrder() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try {
            Scoreboard scoreboard = new Scoreboard(matchFactory);
            scoreboard.clear();

            Map<String, AbstractMap.SimpleEntry<Integer, Integer>> matchScores = new HashMap<>();
            matchScores.put("Mexico-Canada", new AbstractMap.SimpleEntry<>(0, 5));
            matchScores.put("Spain-Brazil", new AbstractMap.SimpleEntry<>(10, 2));
            matchScores.put("Germany-France", new AbstractMap.SimpleEntry<>(2, 2));
            matchScores.put("Uruguay-Italy", new AbstractMap.SimpleEntry<>(6, 6));
            matchScores.put("Argentina-Australia", new AbstractMap.SimpleEntry<>(3, 1));

            Team<?>[] teams = getTeams();
            List<MatchInterface> createdMatches = Collections.synchronizedList(new ArrayList<>());
            CountDownLatch updateLatch = new CountDownLatch(matchScores.size());


            matchScores.forEach((key, value) -> {
                String[] teamNames = key.split("-");
                Team<?> homeTeam = findTeamByName(teams, teamNames[0]);
                Team<?> awayTeam = findTeamByName(teams, teamNames[1]);

                executorService.submit(() -> {
                    MatchInterface match = matchFactory.createMatchBuilder(homeTeam, awayTeam).state(MatchState.forInProgressState()).build();
                    scoreboard.addMatch(match);
                    scoreboard.updateScore(match, value.getKey(), value.getValue());
                    createdMatches.add(match);
                    updateLatch.countDown();
                });
            });

            updateLatch.await();

            List<MatchInterface> summary = scoreboard.getSummary();
            List<MatchInterface> sortedMatches = createdMatches.stream()
                    .sorted(Comparator.comparingInt(MatchInterface::getTotalScore).reversed()
                            .thenComparing(MatchInterface::getCreationTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            assertEquals("Should have 5 matches in summary", 5, summary.size());
            assertEquals("Should have 5 matches in summary", 5, createdMatches.size());
            assertEquals("The sorted lists do not match", sortedMatches, summary);
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        }
    }

    private Team<?> findTeamByName(Team<?>[] teams, String name) {
        for (Team<?> team : teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        throw new IllegalArgumentException("Team with name " + name + " not found");
    }

    private Team<?>[] getTeams() {
        Team<?>[] teams = {
                FootballTeam.builder().name("Mexico").build(),
                FootballTeam.builder().name("Canada").build(),
                FootballTeam.builder().name("Spain").build(),
                FootballTeam.builder().name("Brazil").build(),
                FootballTeam.builder().name("Germany").build(),
                FootballTeam.builder().name("France").build(),
                FootballTeam.builder().name("Uruguay").build(),
                FootballTeam.builder().name("Italy").build(),
                FootballTeam.builder().name("Argentina").build(),
                FootballTeam.builder().name("Australia").build()
        };
        return teams;
    }
}
