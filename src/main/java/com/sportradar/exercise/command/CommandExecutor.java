package com.sportradar.exercise.command;

import com.sportradar.exercise.scoring.Scoreboard;

public class CommandExecutor {
    private final Scoreboard scoreboard;

    public CommandExecutor(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void executeCommand(MatchCommand command) {
        command.execute();
    }
}
