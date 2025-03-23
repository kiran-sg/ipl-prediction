package com.ipl.prediction.iplprediction.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PredictionDto {

    private Long predictionId;
    private String matchId;
    private String userId;
    private IplUserDto user;
    private String tossPredicted;
    private String firstInnScorePredicted;
    private String teamPredicted;
    private String momPredicted;
    private String mostRunsScorerPredicted;
    private String mostWicketsTakerPredicted;
    private LocalDateTime predictionTime;
    private String tossWon;
    private String teamWon;
    private String mom;
    private String mostRunsScorer;
    private String mostWicketsTaker;
    private Integer points;
}
