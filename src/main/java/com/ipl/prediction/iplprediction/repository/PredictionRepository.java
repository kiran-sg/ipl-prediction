package com.ipl.prediction.iplprediction.repository;

import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    @Query("SELECT u, COALESCE(SUM(p.points), 0) as totalPoints " +
            "FROM Prediction p JOIN p.user u " +
            "WHERE u.location = ?1 " +
            "GROUP BY u.id " +
            "ORDER BY totalPoints DESC")
    List<Object[]> getLeaderboardByLocation(String location);

    /**
     * Find a prediction by user and match ID.
     *
     * @param user    The IplUser entity.
     * @param matchId The match ID.
     * @return An Optional containing the prediction if found.
     */
    Optional<Prediction> findByUserAndMatchId(IplUser user, String matchId);

    Optional<List<Prediction>> findAllByMatchId(String matchId);

    Optional<List<Prediction>> findAllByUser(IplUser user);

    //get list of predictions by match ids
    Optional<List<Prediction>> findAllByMatchIdIn(List<String> matchIds);

    //get list of predictions by user and match ids
    Optional<List<Prediction>> findAllByUserAndMatchIdIn(IplUser user, List<String> matchIds);
}
