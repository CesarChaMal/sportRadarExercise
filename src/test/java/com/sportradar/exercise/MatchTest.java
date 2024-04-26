package com.sportradar.exercise;

import org.junit.Test;
import static org.junit.Assert.*;

public class MatchTest {

    @Test(expected = NullPointerException.class)
    public void testMatchCreationWithNullHomeTeam() {
        new Match(null, "AwayTeam");
    }

    @Test(expected = NullPointerException.class)
    public void testMatchCreationWithNullAwayTeam() {
        new Match("HomeTeam", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchCreationWithEmptyHomeTeam() {
        new Match(" ", "AwayTeam");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchCreationWithEmptyAwayTeam() {
        new Match("HomeTeam", " ");
    }
}
