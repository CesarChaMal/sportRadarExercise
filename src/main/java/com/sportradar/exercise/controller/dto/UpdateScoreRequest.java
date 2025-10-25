package com.sportradar.exercise.controller.dto;

public class UpdateScoreRequest {
    private int homeScore;
    private int awayScore;
    
    public UpdateScoreRequest() {}
    
    public UpdateScoreRequest(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }
    
    public int getHomeScore() {
        return homeScore;
    }
    
    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }
    
    public int getAwayScore() {
        return awayScore;
    }
    
    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }
}