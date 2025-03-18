package com.ipl.prediction.iplprediction.repository;

import com.ipl.prediction.iplprediction.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    @Query("SELECT u, SUM(p.points) as totalPoints " +
            "FROM Prediction p JOIN p.user u " +
            "GROUP BY u.id " +
            "ORDER BY totalPoints DESC")
    List<Object[]> getLeaderboard();
}
