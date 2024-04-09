package com.sportradar.exercise.abstract_factory;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.state.NotStartedState;
import com.sportradar.exercise.strategy.ScoringStrategy;

public class FootballMatchFactory implements MatchFactory {
    @Override
    public Match.Builder createMatchBuilder(String homeTeam, String awayTeam) {
        return new Match.Builder(homeTeam, awayTeam)
                .scoringStrategy(ScoringStrategy.forFootballNormalTime())
                .state(new NotStartedState());
    }
}
