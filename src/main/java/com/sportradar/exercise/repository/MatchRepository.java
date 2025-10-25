package com.sportradar.exercise.repository;

import com.sportradar.exercise.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    
    List<MatchEntity> findByStatus(String status);
    
    @Query("SELECT m FROM MatchEntity m WHERE m.status = 'IN_PROGRESS' ORDER BY (m.homeScore + m.awayScore) DESC, m.startTime ASC")
    List<MatchEntity> findActiveMatchesSorted();
    
    List<MatchEntity> findByHomeTeamNameAndAwayTeamName(String homeTeamName, String awayTeamName);
}