package com.sportradar.exercise.strategy;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.strategy_functionall2.ScoringStrategyType;

import java.util.function.BiConsumer;

public interface MatchStrategy {
    ScoringStrategy getScoringStrategy();
    void setScoringStrategy(ScoringStrategy strategy);
    BiConsumer<Match, int[]> getScoringStrategyFunctional1();
    void setScoringStrategyFunctional1(BiConsumer<Match, int[]> strategy);
    ScoringStrategyType getScoringStrategyFunctional2();
    void setScoringStrategyFunctional2(ScoringStrategyType  strategy);
}