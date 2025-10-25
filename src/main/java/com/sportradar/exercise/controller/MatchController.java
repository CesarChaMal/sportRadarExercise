package com.sportradar.exercise.controller;

import com.sportradar.exercise.controller.dto.MatchResponse;
import com.sportradar.exercise.controller.dto.StartMatchRequest;
import com.sportradar.exercise.controller.dto.UpdateScoreRequest;
import com.sportradar.exercise.match.MatchInterface;
import com.sportradar.exercise.service.ScoreboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {
    
    private final ScoreboardService scoreboardService;
    
    public MatchController(ScoreboardService scoreboardService) {
        this.scoreboardService = scoreboardService;
    }
    
    @PostMapping
    public ResponseEntity<MatchResponse> startMatch(@RequestBody StartMatchRequest request) {
        scoreboardService.startMatchAsync(request.getHomeTeam(), request.getAwayTeam());
        return ResponseEntity.ok(new MatchResponse("Match started successfully"));
    }
    
    @PutMapping("/{matchId}/score")
    public ResponseEntity<Void> updateScore(@PathVariable Long matchId, 
                                          @RequestBody UpdateScoreRequest request) {
        MatchInterface match = scoreboardService.getMatchById(matchId);
        if (match != null) {
            scoreboardService.updateScore(match, request.getHomeScore(), request.getAwayScore());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/summary")
    public ResponseEntity<List<MatchResponse>> getSummary() {
        List<MatchInterface> matches = scoreboardService.getSummary();
        List<MatchResponse> response = matches.stream()
            .map(match -> new MatchResponse(
                match.getId(),
                match.getHomeTeam().getName(),
                match.getAwayTeam().getName(),
                match.getHomeScore(),
                match.getAwayScore()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> finishMatch(@PathVariable Long matchId) {
        MatchInterface match = scoreboardService.getMatchById(matchId);
        if (match != null) {
            scoreboardService.finishMatch(match);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}