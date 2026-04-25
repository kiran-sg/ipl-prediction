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
    private Long predictionId;

    @ManyToOne
    @JoinColumn(name = "ipl_user_id", nullable = false)
    private IplUser user;

    @ManyToOne
    @JoinColumn(name = "orange_cap_predicted")
    private IplPlayer orangeCapPredicted;

    @ManyToOne
    @JoinColumn(name = "purple_cap_predicted")
    private IplPlayer purpleCapPredicted;

    @ManyToOne
    @JoinColumn(name = "emerging_player_predicted")
    private IplPlayer emergingPlayerPredicted;

    @ManyToOne
    @JoinColumn(name = "fair_play_team_predicted")
    private IplTeam fairPlayTeamPredicted;

    @ManyToOne
    @JoinColumn(name = "most_fours_predicted")
    private IplPlayer mostFoursPredicted;

    @ManyToOne
    @JoinColumn(name = "most_sixes_predicted")
    private IplPlayer mostSixesPredicted;

    @ManyToOne
    @JoinColumn(name = "most_dot_balls_predicted")
    private IplPlayer mostDotBallsPredicted;

    @ManyToOne
    @JoinColumn(name = "best_bowling_fig_predicted")
    private IplPlayer bestBowlingFigPredicted;

    @ManyToOne
    @JoinColumn(name = "player_of_tournament_predicted")
    private IplPlayer playerOfTournamentPredicted;

    @Column(name = "prediction_time")
    private LocalDateTime predictionTime;

    @Column(name = "points")
    private Integer points;

    @Column(name = "result_updated_time")
    private LocalDateTime resultUpdatedTime;
}
