package com.sportradar.exercise.command.modern;

import com.sportradar.exercise.entity.MatchEntity;
import com.sportradar.exercise.service.MatchPersistenceService;

public class FinishMatchCommand implements MatchCommand<Void> {
    private final MatchPersistenceService persistenceService;
    private final Long matchId;
    private MatchEntity deletedMatch;

    public FinishMatchCommand(MatchPersistenceService persistenceService, Long matchId) {
        this.persistenceService = persistenceService;
        this.matchId = matchId;
    }

    @Override
    public Void execute() {
        deletedMatch = persistenceService.findById(matchId).orElseThrow();
        persistenceService.deleteMatch(matchId);
        return null;
    }

    @Override
    public void undo() {
        if (deletedMatch != null) {
            persistenceService.saveMatch(deletedMatch);
        }
    }
}