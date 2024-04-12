package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

public interface MatchState {
    void startMatch(Match match);
    void finishMatch(Match match);
    boolean canUpdateScore();

    MatchState NOT_STARTED = new NotStartedState();
    MatchState IN_PROGRESS = new InProgressState();
    MatchState FINISHED = new FinishedState();

    static MatchState forNotStartedState() {
        return NOT_STARTED;
    }

    static MatchState forInProgressState() {
        return IN_PROGRESS;
    }

    static MatchState forFinishedState() {
        return FINISHED;
    }
}
