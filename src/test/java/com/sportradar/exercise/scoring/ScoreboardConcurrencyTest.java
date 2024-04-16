package com.sportradar.exercise.scoring;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.match.FootballTeam;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.match.Team;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ScoreboardConcurrencyTest {
    private FootballMatchFactory matchFactory;
    private Scoreboard scoreboard;
    private ExecutorService executorService;

    @Before
    public void setUp() {
        matchFactory = new FootballMatchFactory();
        scoreboard = Scoreboard.getInstance(matchFactory);
        scoreboard.clear();
        executorService = Executors.newFixedThreadPool(10);
    }

    @After
    public void tearDown() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        scoreboard.finishAllMatches();
    }

    @Test
    public void testConcurrentScoreSetting() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
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
        final int threadCount = 20;
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

        assertEquals("Semaphore did not properly limit match starts", 5, activeStarts.get());
    }

    @Test
    public void testConcurrentUpdatesAndSummary() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);

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

        for (int i = 0; i < teams.length; i += 2) {
            int finalI = i;
            service.submit(() -> {
                MatchInterface match = matchFactory .createMatchBuilder(teams[finalI], teams[finalI + 1]).build();
                scoreboard.addMatch(match);
                scoreboard.updateScore(match, (int) (Math.random() * 10), (int) (Math.random() * 10));
            });
        }

        service.submit(() -> {
            List<MatchInterface> summary = scoreboard.getSummary();
            assertTrue("Summary should not be empty", !summary.isEmpty());
            System.out.println("Summary accessed");
        });

        service.shutdown();
        assertTrue(service.awaitTermination(1, TimeUnit.MINUTES));
    }

    @Test
    public void testConcurrentUpdatesAndSummaryOrder() throws InterruptedException {
        Map<String, AbstractMap.SimpleEntry<Integer, Integer>> matchScores = new HashMap<>();
        matchScores.put("Mexico-Canada", new AbstractMap.SimpleEntry<>(0, 5));
        matchScores.put("Spain-Brazil", new AbstractMap.SimpleEntry<>(10, 2));
        matchScores.put("Germany-France", new AbstractMap.SimpleEntry<>(2, 2));
        matchScores.put("Uruguay-Italy", new AbstractMap.SimpleEntry<>(6, 6));
        matchScores.put("Argentina-Australia", new AbstractMap.SimpleEntry<>(3, 1));

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

        CountDownLatch updateLatch = new CountDownLatch(teams.length / 2);

        for (int i = 0; i < teams.length; i += 2) {
            final int index = i;
            String matchKey = teams[index].getName() + "-" + teams[index + 1].getName();
            AbstractMap.SimpleEntry<Integer, Integer> scores = matchScores.get(matchKey);

            executorService.submit(() -> {
                MatchInterface match = matchFactory.createMatchBuilder(teams[index], teams[index + 1]).build();
                scoreboard.addMatch(match);
                scoreboard.updateScore(match, scores.getKey(), scores.getValue());
                updateLatch.countDown();
            });
        }

        updateLatch.await();

        List<MatchInterface> summary = scoreboard.getSummary();
        assertEquals("Should have 5 matches in summary", 5, summary.size());
        assertEquals("Uruguay vs Italy should be first", "Uruguay", summary.get(0).getHomeTeam().getName());
        assertEquals("Spain vs Brazil should be second", "Spain", summary.get(1).getHomeTeam().getName());
        assertEquals("Mexico vs Canada should be third", "Mexico", summary.get(2).getHomeTeam().getName());
        assertEquals("Argentina vs Australia should be fourth", "Argentina", summary.get(3).getHomeTeam().getName());
        assertEquals("Germany vs France should be fifth", "Germany", summary.get(4).getHomeTeam().getName());
    }
}
