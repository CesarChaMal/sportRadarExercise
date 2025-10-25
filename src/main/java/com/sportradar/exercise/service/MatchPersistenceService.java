package com.sportradar.exercise.service;

import com.sportradar.exercise.entity.MatchEntity;
import com.sportradar.exercise.exception.MatchNotFoundException;
import com.sportradar.exercise.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MatchPersistenceService {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchPersistenceService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public MatchEntity saveMatch(MatchEntity match) {
        return matchRepository.save(match);
    }

    public Optional<MatchEntity> findById(Long id) {
        return matchRepository.findById(id);
    }

    public List<MatchEntity> findActiveMatches() {
        return matchRepository.findActiveMatchesSorted();
    }

    public List<MatchEntity> findByStatus(String status) {
        return matchRepository.findByStatus(status);
    }

    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }

    public List<MatchEntity> findAllMatches() {
        return matchRepository.findAll();
    }

    public MatchEntity updateScore(Long id, int homeScore, int awayScore) {
        Optional<MatchEntity> match = matchRepository.findById(id);
        if (match.isPresent()) {
            MatchEntity entity = match.get();
            entity.setHomeScore(homeScore);
            entity.setAwayScore(awayScore);
            return matchRepository.save(entity);
        }
        throw new MatchNotFoundException(id);
    }
}