package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;
import com.sportradar.exercise.observer.MatchEventNotifier;
import com.sportradar.exercise.state.MatchState;

public class MatchStateController implements StateManagement {
    private Match match;
    private MatchEventNotifier<MatchChangeEvent> matchEventNotifier;

    public MatchStateController(Match match, MatchEventNotifier<MatchChangeEvent> matchEventNotifier) {
        this.match = match;
        this.matchEventNotifier = matchEventNotifier;
    }

    public void startMatch() {
        if (match.getState() == MatchState.NOT_STARTED) {
            match.setState(MatchState.IN_PROGRESS);
        } else {
            throw new UnsupportedOperationException("Match cannot start because it is not in the NOT_STARTED state.");
        }
    }

    public void finishMatch() {
        if (match.getState() == MatchState.IN_PROGRESS) {
            match.setState(MatchState.FINISHED);
        } else {
            throw new UnsupportedOperationException("Match cannot finish because it is not in the IN_PROGRESS state.");
        }
    }

    public void pauseMatch() {
        if (match.getState() == MatchState.IN_PROGRESS) {
            match.setState(MatchState.IN_PAUSED);
        } else {
            throw new UnsupportedOperationException("Match cannot be paused because it is not in the IN_PROGRESS state.");
        }
    }

    public void resumeMatch() {
        if (match.getState() == MatchState.IN_PAUSED) {
            match.setState(MatchState.IN_PROGRESS);
        } else {
            throw new UnsupportedOperationException("Match cannot resume because it is not in the PAUSED state.");
        }
    }

    private void notifyStateChange(EventType eventType) {
        matchEventNotifier.notifyObservers(new MatchChangeEvent(match, eventType));
    }
}
