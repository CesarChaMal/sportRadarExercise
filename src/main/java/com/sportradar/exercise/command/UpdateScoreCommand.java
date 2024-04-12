package com.sportradar.exercise.command;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.MatchInterface;

public class UpdateScoreCommand implements MatchCommand {
    private final MatchInterface match;

    public UpdateScoreCommand(MatchInterface match, int homeScore, int awayScore) {
        this.match = match;
        this.match.setHomeScore(homeScore);
        this.match.setAwayScore(awayScore);
    }

    @Override
    public void execute() {
        if (!match.getState().canUpdateScore()) {
            throw new IllegalStateException("Score update is blocked in the current match state.");
        }
        match.updateScore(this.match.getHomeScore(), this.match.getAwayScore());
    }
}
