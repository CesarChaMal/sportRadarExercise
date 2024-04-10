package com.sportradar.exercise.scoring;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.command.UpdateScoreCommand;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.state.InProgressState;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.state.NotStartedState;
import com.sportradar.exercise.strategy.ScoringStrategy;
import com.sportradar.exercise.strategy_functionall1.ScoringStrategiesFunctional1;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class ScoreboardTest {
    private Scoreboard scoreboard;
    private MatchFactory matchFactory;

    @Before
    public void setUp() {
        matchFactory = new FootballMatchFactory();
        scoreboard = new Scoreboard(matchFactory);
    }

    @After
    public void tearDown() {
        scoreboard.getSummary().forEach(match -> {
            if (!(match.getState() instanceof NotStartedState)) {
                scoreboard.finishMatch(match);
            }
        });
    }

    @Test
    public void testCreateMatch() {
        MatchInterface match = matchFactory.createMatchBuilder("Home1", "Away1").scoringStrategy(ScoringStrategy.forFootballNormalTime()).build();
        scoreboard.addMatch(match);
        var summary = scoreboard.getSummary();
        assertEquals("Expected exactly 1 match in summary", 1, summary.size());
        assertEquals("Home1", summary.get(0).getHomeTeam());
        assertEquals("Away1", summary.get(0).getAwayTeam());
        assertEquals(0, summary.get(0).getHomeScore());
        assertEquals(0, summary.get(0).getAwayScore());
    }

    @Test
    public void testStartMatch() {
        scoreboard.startMatch("Home1", "Away1");
        var summary = scoreboard.getSummary();
        assertEquals("Expected exactly 1 match in summary", 1, summary.size());

        var match = summary.get(0);
        assertEquals("Home1", match.getHomeTeam());
        assertEquals("Away1", match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    public void testUpdateScore() {
        scoreboard.startMatch("Home2", "Away2");
        Optional<MatchInterface> optionalMatch = scoreboard.getMatch("Home2", "Away2");

        assertTrue("Match should exist", optionalMatch.isPresent());

        optionalMatch.ifPresent(match -> {
            match.setState(MatchState.forInProgressState());
            match.setScoringStrategy(ScoringStrategy.forFootballNormalTime());
            UpdateScoreCommand updateScoreCommand = new UpdateScoreCommand(match, 2, 2);
            updateScoreCommand.execute();

            MatchInterface updatedMatch = scoreboard.getMatch("Home2", "Away2").orElseThrow(() -> new AssertionError("Match not found"));

            assertEquals("Home score not updated as expected", 2, updatedMatch.getHomeScore());
            assertEquals("Away score not updated as expected", 2, updatedMatch.getAwayScore());
        });
    }

    @Test
    public void testFinishMatch() {
        scoreboard.startMatch("Home3", "Away3");
        Optional<MatchInterface> match = scoreboard.getMatch("Home3", "Away3");
        assertNotNull("Match should exist before finishing", match.isPresent());
        scoreboard.finishMatch(match.get());
        assertTrue("Match should be finished and removed", scoreboard.getSummary().isEmpty());
    }

    @Test
    public void testGetSummary() {
        scoreboard.startMatch("Home1", "Away1");
        scoreboard.startMatch("Home2", "Away2");

        Optional<MatchInterface> match1 = scoreboard.getMatch("Home1", "Away1");
        assertNotNull("Match1 should not be null", match1.get());
        Optional<MatchInterface> match2 = scoreboard.getMatch("Home2", "Away2");
        assertNotNull("Match2 should not be null", match2.get());

        setSettingForMatch(match1);
        setSettingForMatch(match2);

        scoreboard.updateScore(match1.get(), 0, 5);
        scoreboard.updateScore(match2.get(), 10, 2);

        var summary = scoreboard.getSummary();
        assertTrue("Summary should contain both matches", summary.size() == 2);
        assertTrue("The first match should have the higher total score or be the most recently started", summary.get(0).getTotalScore() >= summary.get(1).getTotalScore());
        assertEquals("The first match should have the higher total score", 12, summary.get(0).getTotalScore());
    }

    private void setSettingForMatch(Optional<MatchInterface> optionalMatch) {
        optionalMatch.ifPresent(match -> {
            match.setState(MatchState.forInProgressState());
            match.setScoringStrategy(ScoringStrategy.forFootballNormalTime());
        });
    }

    @Test
    public void testSummaryOrderWithMultipleMatches() {
        getMatch("Mexico", "Canada", 0, 5);
        getMatch("Spain", "Brazil", 10, 2);
        getMatch("Germany" ,"France", 2, 2);
        getMatch("Uruguay","Italy", 6, 6);
        getMatch("Argentina","Australia", 3, 1);

        var summary = scoreboard.getSummary();

        assertEquals("Uruguay vs Italy should be first", "Uruguay", summary.get(0).getHomeTeam());
        assertEquals("Spain vs Brazil should be second", "Spain", summary.get(1).getHomeTeam());
        assertEquals("Mexico vs Canada should be third", "Mexico", summary.get(2).getHomeTeam());
        assertEquals("Argentina vs Australia should be fourth", "Argentina", summary.get(3).getHomeTeam());
        assertEquals("Germany vs France should be fifth", "Germany", summary.get(4).getHomeTeam());
    }

    private void getMatch(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        MatchInterface match = matchFactory.createMatchBuilder(homeTeam, awayTeam).build();
        match.setState(MatchState.forInProgressState());
        scoreboard.addMatch(match);
        scoreboard.updateScore(match, homeScore, awayScore);
    }
}
