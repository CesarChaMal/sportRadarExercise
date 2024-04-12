package com.sportradar.exercise.match;

public class BasketballMatch extends Match {

    private BasketballMatch(Builder builder) {
        super(builder);
    }

    public void scorePoints(BasketballPlayer scorer, int points) {
        BasketballEventManager manager = (BasketballEventManager) getEventManager();
        if (manager == null) {
            throw new IllegalStateException("Event manager is not configured for basketball");
        }
        manager.addPointsScoredEvent(scorer, points);
//        notifyObservers(new MatchChangeEvent(this));
    }

    public static class Builder extends Match.Builder<BasketballMatch.Builder> {
        public Builder(Team<?> homeTeam, Team<?> awayTeam) {
            super(homeTeam, awayTeam);
        }
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public BasketballMatch build() {
            return new BasketballMatch(this);
        }
    }
}
