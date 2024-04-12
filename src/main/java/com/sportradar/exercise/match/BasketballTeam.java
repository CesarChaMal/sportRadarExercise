package com.sportradar.exercise.match;

public class BasketballTeam extends Team<BasketballPlayer> {

    private BasketballTeam(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Team.Builder<BasketballPlayer> {
        public Builder name(String name) {
            super.name(name);
            return this;
        }

        public BasketballTeam build() {
            return new BasketballTeam(this);
        }
    }
}
