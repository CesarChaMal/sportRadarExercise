package com.sportradar.exercise.strategy;

import com.sportradar.exercise.match.Match;

public interface ScoringStrategy {
    void calculateScore(Match match, int homeScore, int awayScore);
}