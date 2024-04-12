package com.sportradar.exercise.match;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TeamBuilderTest {

    @Test
    public void teamBuilderCreatesCorrectTeam() {
        String teamName = "Lakers";
        Team<BasketballPlayer> team = new Team.Builder<BasketballPlayer>()
                .name(teamName)
                .build();

        assertEquals("Team name should match the builder input", teamName, team.getName());
    }
}
