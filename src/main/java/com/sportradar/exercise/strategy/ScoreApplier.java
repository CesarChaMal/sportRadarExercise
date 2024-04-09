package com.sportradar.exercise.strategy;

import com.sportradar.exercise.match.Match;

public interface ScoreApplier {
    void applyScore(Match match, int homeScore, int awayScore);
}
