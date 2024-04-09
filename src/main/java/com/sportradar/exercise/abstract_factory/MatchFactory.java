package com.sportradar.exercise.abstract_factory;

import com.sportradar.exercise.match.Match;

public interface MatchFactory {
    Match.Builder createMatchBuilder(String homeTeam, String awayTeam);
}
