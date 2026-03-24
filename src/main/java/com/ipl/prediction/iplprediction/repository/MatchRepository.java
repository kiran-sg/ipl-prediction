package com.ipl.prediction.iplprediction.repository;

import com.ipl.prediction.iplprediction.entity.IplMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<IplMatch, Long> {
    Optional<IplMatch> findByMatchNo(String matchNo);

    @Query("SELECT m FROM IplMatch m ORDER BY m.dateTime")
    List<IplMatch> findAllOrderByDateTime();
}
