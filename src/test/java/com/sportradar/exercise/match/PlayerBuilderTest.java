package com.sportradar.exercise.match;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerBuilderTest {

    @Test
    public void playerBuilderCreatesCorrectPlayer() {
        String playerName = "LeBron James";
        FootballPlayer player = new FootballPlayer.Builder()
                .name(playerName)
                .position("Forward")
                .build();

        assertEquals("Player name should match the builder input", playerName, player.getName());
        assertEquals("Player position should be 'Forward'", "Forward", player.getPosition());
    }
}
