package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

public class InProgressState implements MatchState {
    @Override
    public void updateScore(Match match, int homeScore, int awayScore) {
        match.updateScore(homeScore, awayScore);
    }

    @Override
    public void finishMatch(Match match) {
        match.setState(new FinishedState());
    }

    @Override
    public boolean canUpdateScore() {
        return true;
    }
}
