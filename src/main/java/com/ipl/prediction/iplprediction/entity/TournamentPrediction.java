package com.ipl.prediction.iplprediction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ipl_tournament_predictions")
public class TournamentPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prediction_id")
    private Long predictionId; // Primary key

    @ManyToOne
    @JoinColumn(name = "ipl_user_id", nullable = false) // Foreign key to IplUser
    private IplUser user; // Many-to-one relationship with IplUser

    @Column(name = "orange_cap_predicted")
    private String orangeCapPredicted; // Predicted Orange Cap Winner

    @Column(name = "purple_cap_predicted")
    private String purpleCapPredicted; // Predicted Purple Cap Winner

    @Column(name = "emerging_player_predicted")
    private String emergingPlayerPredicted; // Predicted Emerging Player of the Season

    @Column(name = "fair_play_team_predicted")
    private String fairPlayTeamPredicted; // Predicted Fair Play Team of the Season

    @Column(name = "most_fours_predicted")
    private String mostFoursPredicted; // Predicted player with most fours

    @Column(name = "most_sixes_predicted")
    private String mostSixesPredicted; // Predicted player with most sixes

    @Column(name = "most_dot_balls_predicted")
    private String mostDotBallsPredicted; // Predicted player with most dot balls

    @Column(name = "best_bowling_fig_predicted")
    private String bestBowlingFigPredicted; // Predicted player with best bowling figures

    @Column(name = "prediction_time")
    private LocalDateTime predictionTime; // Timestamp of the prediction

    @Column(name = "points")
    private Integer points; // Points earned for the prediction

    @Column(name = "result_updated_time")
    private LocalDateTime resultUpdatedTime; //Result update time
}
