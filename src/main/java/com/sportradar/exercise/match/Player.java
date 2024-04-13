package com.sportradar.exercise.match;

public abstract class Player {
    private final String name;
    private final String position;
    private final Team<?> team;

    protected Player(Builder<?> builder) {
        this.name = builder.name;
        this.position = builder.position;
        this.team = builder.team;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public Team<?> getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }

    public abstract static class Builder<T extends Builder<T>> {
        private String name;
        private String position;
        private Team<?> team;

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T position(String position) {
            this.position = position;
            return self();
        }

        public T team(Team<?> team) {
            this.team = team;
            return self();
        }

        protected abstract T self();

        public abstract Player build();
    }
}
