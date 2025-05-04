package com.ipl.prediction.iplprediction.response;

import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.dto.TournamentPredictionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PredictionResponse {
    private String message;
    private boolean status;
    private boolean invalidUser;
    private PredictionDto prediction;
    private TournamentPredictionDto tournamentPrediction;
}
