package com.ipl.prediction.iplprediction.repository;

import com.ipl.prediction.iplprediction.entity.TournamentResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentResultRepository extends JpaRepository<TournamentResult, Long> {
}
