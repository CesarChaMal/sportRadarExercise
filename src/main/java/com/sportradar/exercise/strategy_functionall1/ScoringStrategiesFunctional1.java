package com.sportradar.exercise.strategy_functionall1;

import java.util.function.BiConsumer;

import com.sportradar.exercise.match.Match;

public class ScoringStrategiesFunctional1 {
    public static final BiConsumer<Match, int[]> defaultScoringStrategy = (match, scores) -> {
        match.setHomeScore(scores[0]);
        match.setAwayScore(scores[1]);
    };

    public static final BiConsumer<Match, int[]> forFootballNormalTimeScoringStrategy = defaultScoringStrategy::accept;

    public static final BiConsumer<Match, int[]> forFootballExtraTimeScoringStrategy = (match, scores) -> {
        match.setHomeScore(match.getHomeScore() + scores[0]);
        match.setAwayScore(match.getAwayScore() + scores[1]);
    };

    public static final BiConsumer<Match, int[]> forBasketballNormalTimeScoringStrategy = defaultScoringStrategy::accept;

    public static final BiConsumer<Match, int[]> forBasketballExtraTimeScoringStrategy = (match, scores) -> {
        match.setHomeScore(match.getHomeScore() + scores[0]);
        match.setAwayScore(match.getAwayScore() + scores[1]);
    };
}
