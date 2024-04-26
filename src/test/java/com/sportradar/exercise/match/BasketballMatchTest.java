package com.sportradar.exercise.match;

import com.sportradar.exercise.match.BasketballMatch;
import com.sportradar.exercise.match.Team;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasketballMatchTest {

    @Test(expected = NullPointerException.class)
    public void testBasketballMatchCreationWithNullHomeTeam() {
        Team<?> nullHomeTeam = null;
        new BasketballMatch.Builder(nullHomeTeam, new Team.Builder<BasketballPlayer>().name("AwayTeam").build()).build();
    }

    @Test(expected = NullPointerException.class)
    public void testBasketballMatchCreationWithNullAwayTeam() {
        Team<?> nullAwayTeam = null;
        new BasketballMatch.Builder(new Team.Builder<BasketballPlayer>().name("HomeTeam").build(), nullAwayTeam).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasketballMatchCreationWithEmptyHomeTeam() {
        new BasketballMatch.Builder(new Team.Builder<BasketballPlayer>().name(" ").build(), new Team.Builder<BasketballPlayer>().name("AwayTeam").build()).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasketballMatchCreationWithEmptyAwayTeam() {
        new BasketballMatch.Builder(new Team.Builder<BasketballPlayer>().name("HomeTeam").build(), new Team.Builder<BasketballPlayer>().name(" ").build()).build();
    }
}
