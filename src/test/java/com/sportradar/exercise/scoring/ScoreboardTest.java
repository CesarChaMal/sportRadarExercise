package com.sportradar.exercise.scoring;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import com.sportradar.exercise.command.UpdateScoreCommand;
import com.sportradar.exercise.match.FootballTeam;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.match.Team;
import com.sportradar.exercise.state.MatchState;
import com.sportradar.exercise.state.NotStartedState;
import com.sportradar.exercise.strategy.ScoringStrategy;
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
        Team<?> homeTeam = FootballTeam.builder().name("Home1").build();
        Team<?> awayTeam = FootballTeam.builder().name("Away1").build();

        MatchInterface match = matchFactory.createMatchBuilder(homeTeam, awayTeam)
                .scoringStrategy(ScoringStrategy.forFootballNormalTime()).build();
        scoreboard.addMatch(match);

        var summary = scoreboard.getSummary();
        assertEquals("Expected exactly 1 match in summary", 1, summary.size());
        assertEquals("Home1", summary.get(0).getHomeTeam().getName());
        assertEquals("Away1", summary.get(0).getAwayTeam().getName());
        assertEquals(0, summary.get(0).getHomeScore());
        assertEquals(0, summary.get(0).getAwayScore());
    }

    @Test
    public void testStartMatch() {
        Team<?> homeTeam = FootballTeam.builder().name("Home1").build();
        Team<?> awayTeam = FootballTeam.builder().name("Away1").build();

        MatchInterface match = matchFactory.createMatchBuilder(homeTeam, awayTeam)
                .scoringStrategy(ScoringStrategy.forFootballNormalTime()).build();
        scoreboard.addMatch(match);

        var summary = scoreboard.getSummary();
        assertEquals("Expected exactly 1 match in summary", 1, summary.size());

        MatchInterface retrievedMatch = summary.get(0);
        assertEquals("Home1", retrievedMatch.getHomeTeam().getName());
        assertEquals("Away1", retrievedMatch.getAwayTeam().getName());
        assertEquals(0, retrievedMatch.getHomeScore());
        assertEquals(0, retrievedMatch.getAwayScore());
    }

    @Test
    public void testUpdateScore() {
        Team<?> homeTeam = FootballTeam.builder().name("Home2").build();
        Team<?> awayTeam = FootballTeam.builder().name("Away2").build();

        MatchInterface match = matchFactory.createMatchBuilder(homeTeam, awayTeam)
                .scoringStrategy(ScoringStrategy.forFootballNormalTime()).build();
        scoreboard.addMatch(match);
//        match.setState(MatchState.forInProgressState());
        match.startMatch();

        UpdateScoreCommand updateScoreCommand = new UpdateScoreCommand(match, 2, 2);
        updateScoreCommand.execute();

        Optional<MatchInterface> updatedMatchOpt = scoreboard.getMatch(match.getHomeTeam(), match.getAwayTeam());

        assertTrue("Match should exist", updatedMatchOpt.isPresent());
        MatchInterface updatedMatch = updatedMatchOpt.get();

        assertEquals("Home score not updated as expected", 2, updatedMatch.getHomeScore());
        assertEquals("Away score not updated as expected", 2, updatedMatch.getAwayScore());
    }

    @Test
    public void testFinishMatch() {
        Team<?> home = FootballTeam.builder().name("Home3").build();
        Team<?> away = FootballTeam.builder().name("Away3").build();

        MatchInterface match = matchFactory.createMatchBuilder(home, away)
                .scoringStrategy(ScoringStrategy.forFootballNormalTime()).build();
        scoreboard.addMatch(match);

        match.setState(MatchState.forInProgressState());

        Optional<MatchInterface> retrievedMatch = scoreboard.getMatch(home, away);
        assertTrue("Match should exist before finishing", retrievedMatch.isPresent());

        scoreboard.finishMatch(retrievedMatch.get());

        assertTrue("Match should be finished and removed", scoreboard.getSummary().isEmpty());
    }

    @Test
    public void testGetSummary() {
        Team<?> home1 = FootballTeam.builder().name("Home1").build();
        Team<?> away1 = FootballTeam.builder().name("Away1").build();
        Team<?> home2 = FootballTeam.builder().name("Home2").build();
        Team<?> away2 = FootballTeam.builder().name("Away2").build();

        MatchInterface match1 = matchFactory.createMatchBuilder(home1, away1)
                .scoringStrategy(ScoringStrategy.forFootballNormalTime()).build();
        MatchInterface match2 = matchFactory.createMatchBuilder(home2, away2)
                .scoringStrategy(ScoringStrategy.forFootballNormalTime()).build();

        scoreboard.addMatch(match1);
        scoreboard.addMatch(match2);

//        match1.setState(MatchState.forInProgressState());
        match1.startMatch();
//        match2.setState(MatchState.forInProgressState());
        match2.startMatch();
        scoreboard.updateScore(match1, 0, 5);
        scoreboard.updateScore(match2, 10, 2);

        var summary = scoreboard.getSummary();
        assertEquals("Expected exactly 2 matches in summary", 2, summary.size());
        assertTrue("The first match should have the higher total score or be the most recently started",
                summary.get(0).getTotalScore() >= summary.get(1).getTotalScore());
        assertEquals("The first match should have the higher total score", 12, summary.get(0).getTotalScore());
    }

    private void setSettingForMatch(Optional<MatchInterface> optionalMatch) {
        optionalMatch.ifPresent(match -> {
//            match.setState(MatchState.forInProgressState());
            match.startMatch();
            match.setScoringStrategy(ScoringStrategy.forFootballNormalTime());
        });
    }

    @Test
    public void testSummaryOrderWithMultipleMatches() {
        Team<?> mexico = FootballTeam.builder().name("Mexico").build();
        Team<?> canada = FootballTeam.builder().name("Canada").build();
        Team<?> spain = FootballTeam.builder().name("Spain").build();
        Team<?> brazil = FootballTeam.builder().name("Brazil").build();
        Team<?> germany = FootballTeam.builder().name("Germany").build();
        Team<?> france = FootballTeam.builder().name("France").build();
        Team<?> uruguay = FootballTeam.builder().name("Uruguay").build();
        Team<?> italy = FootballTeam.builder().name("Italy").build();
        Team<?> argentina = FootballTeam.builder().name("Argentina").build();
        Team<?> australia = FootballTeam.builder().name("Australia").build();

        getMatch(mexico, canada, 0, 5);
        getMatch(spain, brazil, 10, 2);
        getMatch(germany, france, 2, 2);
        getMatch(uruguay, italy, 6, 6);
        getMatch(argentina, australia, 3, 1);

        var summary = scoreboard.getSummary();

        assertEquals("Uruguay vs Italy should be first", "Uruguay", summary.get(0).getHomeTeam().getName());
        assertEquals("Spain vs Brazil should be second", "Spain", summary.get(1).getHomeTeam().getName());
        assertEquals("Mexico vs Canada should be third", "Mexico", summary.get(2).getHomeTeam().getName());
        assertEquals("Argentina vs Australia should be fourth", "Argentina", summary.get(3).getHomeTeam().getName());
        assertEquals("Germany vs France should be fifth", "Germany", summary.get(4).getHomeTeam().getName());
    }

    private void getMatch(Team<?> homeTeam, Team<?> awayTeam, int homeScore, int awayScore) {
        MatchInterface match = matchFactory.createMatchBuilder(homeTeam, awayTeam).build();
//        match.setState(MatchState.forInProgressState());
        match.startMatch();
        scoreboard.addMatch(match);
        scoreboard.updateScore(match, homeScore, awayScore);
    }
}
