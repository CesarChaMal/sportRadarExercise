package com.sportradar.exercise.match;

public class FootballPlayer extends Player {
    private final int goalsScored;

    private FootballPlayer(Builder builder) {
        super(builder);
        this.goalsScored = builder.goalsScored;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public static class Builder extends Player.Builder<Builder> {
        private int goalsScored;

        public Builder goalsScored(int goalsScored) {
            this.goalsScored = goalsScored;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public FootballPlayer build() {
            return new FootballPlayer(this);
        }
    }
}
