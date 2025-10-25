package com.sportradar.exercise.exception;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException(String message) {
        super(message);
    }
    
    public MatchNotFoundException(Long matchId) {
        super("Match not found with id: " + matchId);
    }
}