package com.ipl.prediction.iplprediction.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchResultDto {
    private String matchId;
    private String match;
    private String tossWon;
    private String teamWon;
    private String playerOfTheMatch;
    private String mostRunsScorer;
    private String mostWicketsTaker;
    private String firstInnScore;
}
