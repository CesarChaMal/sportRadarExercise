package com.sportradar.exercise.observer;

public interface Observer<T> {
    void update(T event);
}