package com.sportradar.exercise.command.modern;

import com.sportradar.exercise.entity.MatchEntity;
import com.sportradar.exercise.service.MatchPersistenceService;

public class CreateMatchCommand implements MatchCommand<MatchEntity> {
    private final MatchPersistenceService persistenceService;
    private final String homeTeamName;
    private final String awayTeamName;
    private final String matchType;
    private MatchEntity createdMatch;

    public CreateMatchCommand(MatchPersistenceService persistenceService, String homeTeamName, String awayTeamName, String matchType) {
        this.persistenceService = persistenceService;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.matchType = matchType;
    }

    @Override
    public MatchEntity execute() {
        MatchEntity entity = new MatchEntity(homeTeamName, awayTeamName, matchType);
        entity.setStatus("IN_PROGRESS");
        createdMatch = persistenceService.saveMatch(entity);
        return createdMatch;
    }

    @Override
    public void undo() {
        if (createdMatch != null) {
            persistenceService.deleteMatch(createdMatch.getId());
        }
    }
}