package com.sportradar.exercise.state;

import com.sportradar.exercise.match.Match;

public interface MatchState {
    void startMatch(Match match);
    void finishMatch(Match match);
    void pauseMatch(Match match);
    void resumeMatch(Match match);
    boolean canUpdateScore();
    boolean isValidTransition(MatchState newState);

    MatchState NOT_STARTED = new NotStartedState();
    MatchState IN_PROGRESS = new InProgressState();
    MatchState FINISHED = new FinishedState();
    MatchState IN_PAUSED = new InPauseState();

    static MatchState forNotStartedState() {
        return NOT_STARTED;
    }

    static MatchState forInProgressState() {
        return IN_PROGRESS;
    }

    static MatchState forFinishedState() {
        return FINISHED;
    }

    static MatchState forInPauseState() {
        return IN_PAUSED;
    }
}
