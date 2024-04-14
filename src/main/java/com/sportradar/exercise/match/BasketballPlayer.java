package com.sportradar.exercise.match;

public class BasketballPlayer extends Player {
    private final int pointsScored;
    private final Team<?> team;

    private BasketballPlayer(Builder builder) {
        super(builder);
        this.pointsScored = builder.pointsScored;
        this.team = builder.team;
    }

    public int getPointsScored() {
        return pointsScored;
    }

    public Team<?> getTeam() {
        return team;
    }

    public static class Builder extends Player.Builder<Builder> {
        private int pointsScored;
        private Team<?> team;

        public Builder pointsScored(int pointsScored) {
            this.pointsScored = pointsScored;
            return this;
        }

        public Builder team(Team<?> team) {
            this.team = team;
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
