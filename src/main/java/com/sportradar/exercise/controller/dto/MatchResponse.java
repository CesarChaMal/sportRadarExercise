package com.sportradar.exercise.controller.dto;

public class MatchResponse {
    private Long id;
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private String message;
    
    public MatchResponse() {}
    
    public MatchResponse(String message) {
        this.message = message;
    }
    
    public MatchResponse(Long id, String homeTeam, String awayTeam, int homeScore, int awayScore) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    
    public int getHomeScore() { return homeScore; }
    public void setHomeScore(int homeScore) { this.homeScore = homeScore; }
    
    public int getAwayScore() { return awayScore; }
    public void setAwayScore(int awayScore) { this.awayScore = awayScore; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}