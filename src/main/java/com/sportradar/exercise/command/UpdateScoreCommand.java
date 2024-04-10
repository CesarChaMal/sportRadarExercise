package com.sportradar.exercise.command;

import com.sportradar.exercise.match.MatchInterface;

import static java.lang.System.out;

public class UpdateScoreCommand implements MatchCommand {
    private final MatchInterface match;
    private final int homeScore;
    private final int awayScore;

    public UpdateScoreCommand(MatchInterface match, int homeScore, int awayScore) {
        this.match = match;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    @Override
    public void execute() {
        if (!match.getState().canUpdateScore()) {
            throw new IllegalStateException("Score update is blocked in the current match state.");
        }
        match.updateScore(homeScore, awayScore);
    }
}
