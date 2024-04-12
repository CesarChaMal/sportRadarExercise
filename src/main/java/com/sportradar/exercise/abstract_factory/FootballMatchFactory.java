package com.sportradar.exercise.abstract_factory;

import com.sportradar.exercise.match.FootballEventManager;
import com.sportradar.exercise.match.FootballMatch;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.strategy.ScoringStrategyMode;

public class FootballMatchFactory implements MatchFactory<FootballMatch> {
    @Override
    public FootballMatch.Builder createMatchBuilder(Team<?> homeTeam, Team<?> awayTeam) {
            return (FootballMatch.Builder) new FootballMatch.Builder(homeTeam, awayTeam)
                // Uncomment and configure scoring strategies as needed
//                .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
                //.scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL1)
                //.scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL2)
                //.scoringStrategy(ScoringStrategy.forFootballNormalTime())
                //.scoringStrategyFunctional1(ScoringStrategiesFunctional1.forFootballNormalTimeScoringStrategy)
                //.scoringStrategyFunctional2(ScoringStrategyType.FOOTBALL_NORMAL_TIME)
                .eventManagerFactory(FootballEventManager::new)
//                .eventManagerFactory(match -> new FootballEventManager((FootballMatch) match))
                .state(MatchState.forNotStartedState());
    }
}
