package com.sportradar.exercise.match;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Team<P extends Player> {
    private final String name;
    private List<P> roster;
    private final String logoUrl;
    private final LocalDate foundedDate;
    private final int wins;
    private final int losses;
    private final int draws;

    protected Team(Builder<P> builder) {
        this.name = builder.name;
        this.roster = new ArrayList<>(builder.roster);
        this.logoUrl = builder.logoUrl;
        this.foundedDate = builder.foundedDate;
        this.wins = builder.wins;
        this.losses = builder.losses;
        this.draws = builder.draws;
    }

    public String getName() {
        return name;
    }

    public List<P> getRoster() {
//        return Collections.unmodifiableList(roster);
        return new ArrayList<>(roster);
    }

/*
    public void setRoster(List<P> newRoster) {
        this.roster = new ArrayList<>(newRoster);
    }
*/

    public void addPlayer(P player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.roster.add(player);
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public LocalDate getFoundedDate() {
        return foundedDate;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                '}';
    }

    public static class Builder<P extends Player> {
        private String name;
        private List<P> roster = new ArrayList<>();
        private String logoUrl;
        private LocalDate foundedDate;
        private int wins;
        private int losses;
        private int draws;

        public Builder<P> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<P> logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public Builder<P> foundedDate(LocalDate foundedDate) {
            this.foundedDate = foundedDate;
            return this;
        }

        public Builder<P> wins(int wins) {
            this.wins = wins;
            return this;
        }

        public Builder<P> losses(int losses) {
            this.losses = losses;
            return this;
        }

        public Builder<P> draws(int draws) {
            this.draws = draws;
            return this;
        }

        public Builder<P> addPlayer(P player) {
            this.roster.add(player);
            return this;
        }

        public Builder<P> roster(List<P> roster) {
            this.roster = new ArrayList<>(roster);
            return this;
        }

        public Team<P> build() {
            return new Team<>(this) {};
        }
    }
}
