package com.sportradar.exercise.state;

public interface StateManageable {
    MatchState getState();
    void setState(MatchState state);
}