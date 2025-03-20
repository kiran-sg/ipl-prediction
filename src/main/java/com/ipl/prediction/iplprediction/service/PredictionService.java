package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.dto.LeaderboardDTO;
import com.ipl.prediction.iplprediction.dto.PredictionDto;

import java.util.List;

public interface PredictionService {
    PredictionDto getPrediction(String userId, String matchId);

    PredictionDto savePrediction(PredictionDto predictionDto);

    List<LeaderboardDTO> getLeaderboard(String location);
}
