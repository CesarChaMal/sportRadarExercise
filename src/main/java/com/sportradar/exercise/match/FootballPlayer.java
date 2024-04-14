package com.sportradar.exercise.match;

public final class FootballPlayer extends Player {
    private final int goalsScored;
    private final Team<?> team;

    private FootballPlayer(Builder builder) {
        super(builder);
        this.goalsScored = builder.goalsScored;
        this.team = builder.team;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public Team<?> getTeam() {
        return team;
    }

    public static class Builder extends Player.Builder<Builder> {
        private int goalsScored;
        private Team<?> team;

        public Builder goalsScored(int goalsScored) {
            this.goalsScored = goalsScored;
            return this;
        }

        public FootballPlayer.Builder team(Team<?> team) {
            this.team = team;
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
