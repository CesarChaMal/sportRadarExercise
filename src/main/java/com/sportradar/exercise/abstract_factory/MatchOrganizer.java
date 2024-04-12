package com.sportradar.exercise.abstract_factory;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.Team;

public class MatchOrganizer {
    private MatchFactory matchFactory;

    public MatchOrganizer(MatchFactory matchFactory) {
        this.matchFactory = matchFactory;
    }

    public Match organizeMatch(Team<?> homeTeam, Team<?> awayTeam) {
        Match.Builder matchBuilder = matchFactory.createMatchBuilder(homeTeam, awayTeam);
        return matchBuilder.build();
    }
}
