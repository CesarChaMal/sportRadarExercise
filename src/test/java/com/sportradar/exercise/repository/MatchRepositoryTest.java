package com.sportradar.exercise.repository;

import com.sportradar.exercise.entity.MatchEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MatchRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MatchRepository matchRepository;

    @Test
    public void testFindByStatus() {
        MatchEntity match = new MatchEntity("Team A", "Team B", "FOOTBALL");
        match.setStatus("IN_PROGRESS");
        entityManager.persistAndFlush(match);

        List<MatchEntity> matches = matchRepository.findByStatus("IN_PROGRESS");
        
        assertEquals(1, matches.size());
        assertEquals("Team A", matches.get(0).getHomeTeamName());
    }

    @Test
    public void testFindActiveMatchesSorted() {
        MatchEntity match1 = new MatchEntity("Team A", "Team B", "FOOTBALL");
        match1.setStatus("IN_PROGRESS");
        match1.setHomeScore(1);
        match1.setAwayScore(0);
        
        MatchEntity match2 = new MatchEntity("Team C", "Team D", "FOOTBALL");
        match2.setStatus("IN_PROGRESS");
        match2.setHomeScore(2);
        match2.setAwayScore(1);
        
        entityManager.persistAndFlush(match1);
        entityManager.persistAndFlush(match2);

        List<MatchEntity> matches = matchRepository.findActiveMatchesSorted();
        
        assertEquals(2, matches.size());
        assertEquals(3, matches.get(0).getHomeScore() + matches.get(0).getAwayScore());
    }
}