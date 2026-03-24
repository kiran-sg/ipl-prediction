package com.ipl.prediction.iplprediction.repository;

import com.ipl.prediction.iplprediction.entity.IplTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<IplTeam, Long> {
    Optional<IplTeam> findByShortName(String shortName);
}
