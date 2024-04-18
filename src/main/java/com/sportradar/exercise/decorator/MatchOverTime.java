package com.sportradar.exercise.decorator;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.strategy.ScoringStrategy;

public class MatchOverTime extends MatchDecorator {
    private ScoringStrategy overtimeScoringStrategy;
    private boolean isOvertimeActive = false;

    protected MatchOverTime(Builder builder) {
        super(builder);
        this.overtimeScoringStrategy = builder.overtimeScoringStrategy;
    }

    public void startOvertime() {
        isOvertimeActive = true;
        System.out.println("Overtime has started.");
        this.match.setScoringStrategy(overtimeScoringStrategy);
    }

    public void endOvertime() {
        isOvertimeActive = false;
        System.out.println("Overtime has ended.");
    }

    public static class Builder extends MatchDecorator.Builder<Builder> {
        private ScoringStrategy overtimeScoringStrategy;

        public Builder(Match match) {
            super(match);
        }

        public Builder withOvertimeScoringStrategy(ScoringStrategy strategy) {
            this.overtimeScoringStrategy = strategy;
            return this;
        }

        @Override
        public MatchOverTime build() {
            return new MatchOverTime(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
