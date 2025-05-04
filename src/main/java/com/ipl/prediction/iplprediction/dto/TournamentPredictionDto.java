package com.ipl.prediction.iplprediction.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TournamentPredictionDto {

    private Long predictionId;
    private String userId;
    private IplUserDto user;
    private String orangeCapPredicted;
    private String purpleCapPredicted;
    private String emergingPlayerPredicted;
    private String fairPlayTeamPredicted;
    private String mostFoursPredicted;
    private String mostSixesPredicted;
    private String mostDotBallsPredicted;
    private String bestBowlingFigPredicted;
    private LocalDateTime predictionTime;
    private Integer points;
    private LocalDateTime resultUpdatedTime;
}
