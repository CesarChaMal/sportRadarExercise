package com.sportradar.exercise.match;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.observer.MatchObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FootballMatchSummaryConcurrencyTest {
    private FootballMatchFactory factory;
    private Team<FootballPlayer> argentina;
    private Team<FootballPlayer> england;
    private FootballMatch match;

    @Before
    public void setUp() {
        factory = new FootballMatchFactory();

        argentina = getFootballTeam("Argentina");
        england = getFootballTeam("England");

        FootballPlayer maradona = getFootballPlayer("Diego Maradona", argentina);
        FootballPlayer shilton = getFootballPlayer("Peter Shilton", england);

        argentina.addPlayer(maradona);
        england.addPlayer(shilton);

        match = createMatch();
        MatchObserver observer = new MatchObserver();
        match.registerObserver(observer);
    }

    private Team<FootballPlayer> getFootballTeam(String name) {
        return new Team.Builder<FootballPlayer>()
                .name(name)
                .build();
    }

    private FootballPlayer getFootballPlayer(String name, Team<FootballPlayer> team) {
        return new FootballPlayer.Builder()
                .name(name)
                .goalsScored(0)
                .team(team)
                .build();
    }

    private FootballMatch createMatch() {
        return factory.createMatchBuilder(argentina, england).build();
    }

    @Test
    public void testConcurrentGoalScoring() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        AtomicInteger goalCount = new AtomicInteger();

        match.startMatch();

        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                match.scoreGoal(argentina.getRoster().get(0), null);
                goalCount.incrementAndGet();
            });
            executor.execute(() -> {
                match.scoreGoal(england.getRoster().get(0), null);
                goalCount.incrementAndGet();
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals("Expected 20 goals scored in total", 20, goalCount.get());
        assertEquals("Expected 20 goals scored in total", 20, match.getTotalScore());
        assertEquals("Match events should reflect 41 events", 41, match.getEvents().size());
    }
}
