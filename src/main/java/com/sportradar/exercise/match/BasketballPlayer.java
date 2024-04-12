package com.sportradar.exercise.match;

public class BasketballPlayer extends Player {
    private final int pointsScored;

    private BasketballPlayer(Builder builder) {
        super(builder);
        this.pointsScored = builder.pointsScored;
    }

    public int getPointsScored() {
        return pointsScored;
    }

    public static class Builder extends Player.Builder<Builder> {
        private int pointsScored;

        public Builder pointsScored(int pointsScored) {
            this.pointsScored = pointsScored;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public BasketballPlayer build() {
            return new BasketballPlayer(this);
        }
    }
}
