package com.sportradar.exercise.command.modern;

import com.sportradar.exercise.entity.MatchEntity;
import com.sportradar.exercise.service.MatchPersistenceService;

public class UpdateScoreCommand implements MatchCommand<MatchEntity> {
    private final MatchPersistenceService persistenceService;
    private final Long matchId;
    private final int newHomeScore;
    private final int newAwayScore;
    private int previousHomeScore;
    private int previousAwayScore;

    public UpdateScoreCommand(MatchPersistenceService persistenceService, Long matchId, int homeScore, int awayScore) {
        this.persistenceService = persistenceService;
        this.matchId = matchId;
        this.newHomeScore = homeScore;
        this.newAwayScore = awayScore;
    }

    @Override
    public MatchEntity execute() {
        MatchEntity match = persistenceService.findById(matchId).orElseThrow();
        previousHomeScore = match.getHomeScore();
        previousAwayScore = match.getAwayScore();
        return persistenceService.updateScore(matchId, newHomeScore, newAwayScore);
    }

    @Override
    public void undo() {
        persistenceService.updateScore(matchId, previousHomeScore, previousAwayScore);
    }
}