package com.ipl.prediction.iplprediction.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TournamentPredictionDto {

    private Long predictionId;
    private String userId;
    private IplUserDto user;
    private Long orangeCapPredictedId;
    private Long purpleCapPredictedId;
    private Long emergingPlayerPredictedId;
    private Long fairPlayTeamPredictedId;
    private Long mostFoursPredictedId;
    private Long mostSixesPredictedId;
    private Long mostDotBallsPredictedId;
    private Long bestBowlingFigPredictedId;
    private Long playerOfTournamentPredictedId;
    private LocalDateTime predictionTime;
    private Integer points;
    private LocalDateTime resultUpdatedTime;
}
