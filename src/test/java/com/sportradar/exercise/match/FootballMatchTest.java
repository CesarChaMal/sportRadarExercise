package com.sportradar.exercise.match;

import com.sportradar.exercise.match.FootballMatch;
import com.sportradar.exercise.match.Team;
import org.junit.Test;
import static org.junit.Assert.*;

public class FootballMatchTest {

    @Test(expected = NullPointerException.class)
    public void testFootballMatchCreationWithNullHomeTeam() {
        Team<?> nullHomeTeam = null;
        new FootballMatch.Builder(nullHomeTeam, new Team.Builder<BasketballPlayer>().name("AwayTeam").build()).build();
    }

    @Test(expected = NullPointerException.class)
    public void testFootballMatchCreationWithNullAwayTeam() {
        Team<?> nullAwayTeam = null;
        new FootballMatch.Builder(new Team.Builder<BasketballPlayer>().name("HomeTeam").build(), nullAwayTeam).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFootballMatchCreationWithEmptyHomeTeam() {
        new FootballMatch.Builder(new Team.Builder<BasketballPlayer>().name(" ").build(), new Team.Builder<BasketballPlayer>().name("AwayTeam").build()).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFootballMatchCreationWithEmptyAwayTeam() {
        new FootballMatch.Builder(new Team.Builder<BasketballPlayer>().name("HomeTeam").build(), new Team.Builder<BasketballPlayer>().name(" ").build()).build();
    }
}
