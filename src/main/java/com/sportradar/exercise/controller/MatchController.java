package com.sportradar.exercise.controller;

import com.sportradar.exercise.dto.CreateMatchRequest;
import com.sportradar.exercise.dto.MatchResponse;
import com.sportradar.exercise.dto.UpdateScoreRequest;
import com.sportradar.exercise.entity.MatchEntity;
import com.sportradar.exercise.exception.MatchNotFoundException;
import com.sportradar.exercise.command.modern.CommandExecutor;
import com.sportradar.exercise.command.modern.CreateMatchCommand;
import com.sportradar.exercise.command.modern.FinishMatchCommand;
import com.sportradar.exercise.command.modern.UpdateScoreCommand;
import com.sportradar.exercise.service.MatchPersistenceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    
    private final MatchPersistenceService persistenceService;
    private final CommandExecutor commandExecutor;
    
    @Autowired
    public MatchController(MatchPersistenceService persistenceService, CommandExecutor commandExecutor) {
        this.persistenceService = persistenceService;
        this.commandExecutor = commandExecutor;
    }
    
    @PostMapping
    public ResponseEntity<MatchResponse> createMatch(@Valid @RequestBody CreateMatchRequest request) {
        CreateMatchCommand command = new CreateMatchCommand(persistenceService, request.homeTeamName(), request.awayTeamName(), request.matchType());
        MatchEntity saved = commandExecutor.executeCommand(command);
        
        MatchResponse response = new MatchResponse(
                saved.getId(),
                saved.getHomeTeamName(),
                saved.getAwayTeamName(),
                saved.getHomeScore(),
                saved.getAwayScore(),
                saved.getStatus()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{matchId}/score")
    public ResponseEntity<MatchResponse> updateScore(@PathVariable Long matchId, @Valid @RequestBody UpdateScoreRequest request) {
        UpdateScoreCommand command = new UpdateScoreCommand(persistenceService, matchId, request.homeScore(), request.awayScore());
        MatchEntity updated = commandExecutor.executeCommand(command);
        MatchResponse response = new MatchResponse(
                updated.getId(),
                updated.getHomeTeamName(),
                updated.getAwayTeamName(),
                updated.getHomeScore(),
                updated.getAwayScore(),
                updated.getStatus()
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/summary")
    public ResponseEntity<List<MatchResponse>> getMatchSummary() {
        List<MatchResponse> summary = persistenceService.findActiveMatches().stream()
                .map(match -> new MatchResponse(
                        match.getId(),
                        match.getHomeTeamName(),
                        match.getAwayTeamName(),
                        match.getHomeScore(),
                        match.getAwayScore(),
                        match.getStatus()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(summary);
    }
    
    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> finishMatch(@PathVariable Long matchId) {
        Optional<MatchEntity> match = persistenceService.findById(matchId);
        if (match.isEmpty()) {
            throw new MatchNotFoundException(matchId);
        }
        FinishMatchCommand command = new FinishMatchCommand(persistenceService, matchId);
        commandExecutor.executeCommand(command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/undo")
    public ResponseEntity<Void> undoLastCommand() {
        commandExecutor.undoLastCommand();
        return ResponseEntity.ok().build();
    }
}