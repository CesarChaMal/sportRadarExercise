package com.sportradar.exercise.scoring;

import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.analytics.MatchSummaryGenerator;
import com.sportradar.exercise.command.CommandExecutor;
import com.sportradar.exercise.command.FinishMatchCommand;
import com.sportradar.exercise.command.StartMatchCommand;
import com.sportradar.exercise.command.UpdateScoreCommand;
import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.match.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Scoreboard {
    private final List<MatchInterface> matches;
    private final CommandExecutor commandExecutor;
    private final MatchSummaryGenerator summaryGenerator;
    private final MatchFactory matchFactory;

    public Scoreboard(MatchFactory matchFactory) {
        matches = new ArrayList<>();
        this.commandExecutor = new CommandExecutor(this);
        this.summaryGenerator = new MatchSummaryGenerator();
        this.matchFactory = matchFactory;
    }

    public Scoreboard(MatchFactory matchFactory, CommandExecutor commandExecutor, MatchSummaryGenerator summaryGenerator) {
        this.matches = new ArrayList<>();
        this.commandExecutor = commandExecutor;
        this.summaryGenerator = summaryGenerator;
        this.matchFactory = matchFactory;
    }

    public void startMatch(Team<?> homeTeam, Team<?> awayTeam) {
        StartMatchCommand startMatchCommand = new StartMatchCommand(this, this.matchFactory, homeTeam, awayTeam);
        commandExecutor.executeCommand(startMatchCommand);
    }

    public void updateScore(MatchInterface match, int homeScore, int awayScore) {
        UpdateScoreCommand updateScoreCommand = new UpdateScoreCommand(match, homeScore, awayScore);
        commandExecutor.executeCommand(updateScoreCommand);
    }

    public void finishMatch(MatchInterface match) {
        FinishMatchCommand finishMatchCommand = new FinishMatchCommand(this, match);
        commandExecutor.executeCommand(finishMatchCommand);
    }

    public void addMatch(MatchInterface match) {
        matches.add(match);
    }

    public void removeMatch(MatchInterface match) {
        matches.remove(match);
    }

    public List<MatchInterface> getMatches() {
//        return Collections.unmodifiableList(matches);
        return new ArrayList<>(matches);
    }

    //O(n log n
    public List<MatchInterface> getSummary() {
        return summaryGenerator.generateSummary(new ArrayList<>(matches));
    }

    public Optional<MatchInterface> getMatch(Team<?> homeTeam, Team<?> awayTeam) {
        return matches.stream()
                .filter(match -> match.getHomeTeam().equals(homeTeam) && match.getAwayTeam().equals(awayTeam))
                .findFirst();
    }
}
