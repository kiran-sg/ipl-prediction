package com.ipl.prediction.iplprediction.repository;

import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.entity.TournamentPrediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TournamentPredictionRepository extends JpaRepository<TournamentPrediction, Long> {

    Optional<TournamentPrediction> findByUser(IplUser user);
}
