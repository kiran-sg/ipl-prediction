package com.ipl.prediction.iplprediction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ipl_predictions") // Table name in the database
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prediction_id")
    private Long predictionId; // Primary key

    @Column(name = "match_id", nullable = false)
    private String matchId; // ID of the match being predicted

    @ManyToOne
    @JoinColumn(name = "ipl_user_id", nullable = false) // Foreign key to IplUser
    private IplUser user; // Many-to-one relationship with IplUser

    @Column(name = "toss_predicted", nullable = false)
    private String tossPredicted;

    @Column(name = "first_inn_score_predicted", nullable = false)
    private String firstInnScorePredicted;// Predicted first innings score

    @Column(name = "team_predicted", nullable = false)
    private String teamPredicted; // Predicted match winner

    @Column(name = "mom_predicted", nullable = false)
    private String momPredicted; // Predicted Man of the Match

    @Column(name = "most_runs_scorer_predicted", nullable = false)
    private String mostRunsScorerPredicted; // Predicted player with most runs

    @Column(name = "most_wickets_taker_predicted", nullable = false)
    private String mostWicketsTakerPredicted; // Predicted player with most wickets

    @Column(name = "prediction_time", nullable = false)
    private LocalDateTime predictionTime; // Timestamp of the prediction

    @Column(name = "toss_won")
    private String tossWon; // Actual toss winner

    @Column(name = "first_inn_score")
    private String firstInnScore;// Actual first innings score

    @Column(name = "team_won")
    private String teamWon; // Actual match winner

    @Column(name = "mom")
    private String mom; // Actual Man of the Match

    @Column(name = "most_runs_scorer")
    private String mostRunsScorer; // Actual player with most runs

    @Column(name = "most_wickets_taker")
    private String mostWicketsTaker; // Actual player with most wickets

    @Column(name = "points")
    private Integer points; // Points earned for the prediction

    @Column(name = "result_updated_time")
    private LocalDateTime resultUpdatedTime; //Result update time
}
