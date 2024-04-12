package com.sportradar.exercise.abstract_factory;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.Team;

public interface MatchFactory<T extends Match> {
    Match.Builder<?> createMatchBuilder(Team<?> homeTeam, Team<?> awayTeam);
}
