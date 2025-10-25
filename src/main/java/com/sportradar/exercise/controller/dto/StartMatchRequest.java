package com.sportradar.exercise.controller.dto;

import com.sportradar.exercise.match.FootballTeam;
import com.sportradar.exercise.match.Team;

public class StartMatchRequest {
    private String homeTeamName;
    private String awayTeamName;
    
    public StartMatchRequest() {}
    
    public StartMatchRequest(String homeTeamName, String awayTeamName) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
    }
    
    public String getHomeTeamName() {
        return homeTeamName;
    }
    
    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }
    
    public String getAwayTeamName() {
        return awayTeamName;
    }
    
    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }
    
    public Team<?> getHomeTeam() {
        return new FootballTeam.Builder().name(homeTeamName).build();
    }
    
    public Team<?> getAwayTeam() {
        return new FootballTeam.Builder().name(awayTeamName).build();
    }
}