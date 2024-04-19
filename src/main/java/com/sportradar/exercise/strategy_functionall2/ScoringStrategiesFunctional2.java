package com.sportradar.exercise.strategy_functionall2;

import java.util.Map;
import java.util.function.BiConsumer;
import com.sportradar.exercise.match.Match;

public class ScoringStrategiesFunctional2 {

    public static final Map<ScoringStrategyType, BiConsumer<Match, int[]>> STRATEGY_MAP = Map.of(
        ScoringStrategyType.DEFAULT, (match, scores) -> {
            match.setHomeScore(scores[0]);
            match.setAwayScore(scores[1]);
        },
        ScoringStrategyType.FOOTBALL_NORMAL_TIME, (match, scores) -> {
            match.setHomeScore(scores[0]);
            match.setAwayScore(scores[1]);
        },
        ScoringStrategyType.FOOTBALL_EXTRA_TIME, (match, scores) -> {
            match.setHomeScore(match.getHomeScore() + scores[0]);
            match.setAwayScore(match.getAwayScore() + scores[1]);
        },
        ScoringStrategyType.BASKETBALL_NORMAL_TIME, (match, scores) -> {
            match.setHomeScore(scores[0]);
            match.setAwayScore(scores[1]);
        },
        ScoringStrategyType.BASKETBALL_EXTRA_TIME, (match, scores) -> {
            match.setHomeScore(match.getHomeScore() + scores[0]);
            match.setAwayScore(match.getAwayScore() + scores[1]);
        }
    );

    public static BiConsumer<Match, int[]> getStrategy(ScoringStrategyType type) {
        return STRATEGY_MAP.getOrDefault(type, (match, scores) -> {
            match.setHomeScore(scores[0]);
            match.setAwayScore(scores[1]);
        });
    }
}
