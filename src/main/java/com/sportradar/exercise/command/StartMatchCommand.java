package com.sportradar.exercise.command;

import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.scoring.Scoreboard;
import com.sportradar.exercise.state.InProgressState;
import com.sportradar.exercise.state.MatchState;

public class StartMatchCommand implements MatchCommand {
    private final Scoreboard scoreboard;
    private final MatchFactory matchFactory;
    private final Team<?> homeTeam;
    private final Team<?> awayTeam;

    public StartMatchCommand(Scoreboard scoreboard, MatchFactory matchFactory, Team<?> homeTeam, Team<?> awayTeam) {
        this.scoreboard = scoreboard;
        this.matchFactory = matchFactory;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    @Override
    public void execute() {
        MatchInterface newMatch = matchFactory.createMatchBuilder(homeTeam, awayTeam).build();
        newMatch.setState(MatchState.forInProgressState());
        scoreboard.addMatch(newMatch);
    }
}
