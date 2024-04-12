package com.sportradar.exercise.match;

public class FootballTeam extends Team<FootballPlayer> {
    private FootballTeam(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Team.Builder<FootballPlayer> {
        public Builder name(String name) {
            super.name(name);
            return this;
        }

        public FootballTeam build() {
            return new FootballTeam(this);
        }
    }
}
