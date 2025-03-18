package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.dto.LeaderboardDTO;
import com.ipl.prediction.iplprediction.dto.PredictionDto;

import java.util.List;

public interface PredictionService {
    PredictionDto savePrediction(PredictionDto predictionDto);

    PredictionDto editPrediction(Long predictionId, PredictionDto updatedPrediction);

    List<LeaderboardDTO> getLeaderboard();
}
