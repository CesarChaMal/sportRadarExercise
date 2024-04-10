package com.sportradar.exercise.command;

import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.scoring.Scoreboard;
import com.sportradar.exercise.state.FinishedState;
import com.sportradar.exercise.state.MatchState;

public class FinishMatchCommand implements MatchCommand {
    private Scoreboard scoreboard;
    private MatchInterface match;

    public FinishMatchCommand(Scoreboard scoreboard, MatchInterface match) {
        this.scoreboard = scoreboard;
        this.match = match;
    }

    @Override
    public void execute() {
        match.setState(MatchState.forFinishedState());
        scoreboard.removeMatch(match);
    }
}
