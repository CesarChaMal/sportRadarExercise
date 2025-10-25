package com.sportradar.exercise.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String homeTeamName;
    
    @Column(nullable = false)
    private String awayTeamName;
    
    @Column(nullable = false)
    private Integer homeScore = 0;
    
    @Column(nullable = false)
    private Integer awayScore = 0;
    
    @Column(nullable = false)
    private String matchType;
    
    @Column(nullable = false)
    private String status = "NOT_STARTED";
    
    @Column(nullable = false)
    private LocalDateTime startTime = LocalDateTime.now();

    public MatchEntity() {}

    public MatchEntity(String homeTeamName, String awayTeamName, String matchType) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.matchType = matchType;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getHomeTeamName() { return homeTeamName; }
    public void setHomeTeamName(String homeTeamName) { this.homeTeamName = homeTeamName; }
    
    public String getAwayTeamName() { return awayTeamName; }
    public void setAwayTeamName(String awayTeamName) { this.awayTeamName = awayTeamName; }
    
    public Integer getHomeScore() { return homeScore; }
    public void setHomeScore(Integer homeScore) { this.homeScore = homeScore; }
    
    public Integer getAwayScore() { return awayScore; }
    public void setAwayScore(Integer awayScore) { this.awayScore = awayScore; }
    
    public String getMatchType() { return matchType; }
    public void setMatchType(String matchType) { this.matchType = matchType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
}