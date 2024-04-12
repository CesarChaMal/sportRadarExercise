package com.sportradar.exercise.match;

import com.sportradar.exercise.observer.MatchChangeEvent;

public class FootballMatch extends Match {
    private FootballMatch(Builder builder) {
        super(builder);
    }

    public void scoreHomeGoal(FootballPlayer scorer, FootballPlayer assistant) {
        incrementHomeScore(1);
        ((FootballEventManager)getEventManager()).addGoalEvent(scorer, assistant);
        notifyObservers(new MatchChangeEvent(this));
    }

    public void scoreAwayGoal(FootballPlayer scorer, FootballPlayer assistant) {
        incrementAwayScore(1);
        ((FootballEventManager)getEventManager()).addGoalEvent(scorer, assistant);
        notifyObservers(new MatchChangeEvent(this));
    }

    public void updateScore(int homeScore, int awayScore) {
        super.updateScore(homeScore, awayScore);
        notifyObservers(new MatchChangeEvent(this));
    }

    public static class Builder extends Match.Builder<Builder> {
        public Builder(Team<?> homeTeam, Team<?> awayTeam) {
            super(homeTeam, awayTeam);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public FootballMatch build() {
            return new FootballMatch(this);
        }
    }
}
