package com.sportradar.exercise.observer;

import com.sportradar.exercise.match.MatchInterface;

public interface Observer {
    void update(MatchInterface match);
}

