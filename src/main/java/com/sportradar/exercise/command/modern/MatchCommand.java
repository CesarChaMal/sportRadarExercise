package com.sportradar.exercise.command.modern;

public interface MatchCommand<T> {
    T execute();
    void undo();
}