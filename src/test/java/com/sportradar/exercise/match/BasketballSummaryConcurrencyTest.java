package com.sportradar.exercise.match;

import com.sportradar.exercise.abstract_factory.BasketballMatchFactory;
import com.sportradar.exercise.observer.MatchObserver;
import com.sportradar.exercise.state.MatchState;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BasketballSummaryConcurrencyTest {
    private BasketballMatchFactory factory;
    private Team<BasketballPlayer> bulls;
    private Team<BasketballPlayer> jazz;
    private BasketballMatch match;

    @Before
    public void setUp() {
        factory = new BasketballMatchFactory();

        bulls =getBasketballTeam("Chicago Bulls");
        jazz = getBasketballTeam("Utah Jazz");

        BasketballPlayer jordan = getBasketballPlayer("Michael Jordan", bulls);
        BasketballPlayer pippen = getBasketballPlayer("Scottie Pippen", bulls);
        BasketballPlayer malone = getBasketballPlayer("Karl Malone", jazz);
        BasketballPlayer stockton = getBasketballPlayer("John Stockton", jazz);

        bulls.addPlayer(jordan);
        bulls.addPlayer(pippen);
        jazz.addPlayer(malone);
        jazz.addPlayer(stockton);

        match = createMatch();
        MatchObserver observer = new MatchObserver();
        match.registerObserver(observer);
    }

    private Team<BasketballPlayer> getBasketballTeam(String name) {
        return new Team.Builder<BasketballPlayer>()
                .name(name)
                .build();
    }

    private BasketballPlayer getBasketballPlayer(String name, Team<BasketballPlayer> team) {
        return  new BasketballPlayer.Builder()
                .name(name)
                .pointsScored(0)
                .team(team)
                .build();
    }

    private BasketballMatch createMatch() {
        return factory.createMatchBuilder(bulls, jazz).build();
    }

    @Test
    public void testConcurrentGoalScoring() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        AtomicInteger poinsCount = new AtomicInteger();

        match.startMatch();

        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                match.scorePoints(bulls.getRoster().get(0), 3);
                poinsCount.addAndGet(3);
            });
            executor.execute(() -> {
                match.scorePoints(jazz.getRoster().get(0), 1);
                poinsCount.incrementAndGet();
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals("Expected 20 points scored in total", 40, poinsCount.get());
        assertEquals("Expected 20 points scored in total", 40, match.getTotalScore());
        assertEquals("Match events should reflect 41 events", 41, match.getEvents().size());
    }
}
