package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.dto.LeaderboardDTO;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.dto.TournamentPredictionDto;
import com.ipl.prediction.iplprediction.response.PredictionResponse;

import java.util.List;

public interface PredictionService {
    PredictionDto getPrediction(String userId, String matchId);

    PredictionDto savePrediction(PredictionDto predictionDto, String userId);

    List<PredictionDto> getPredictionsByUser(String userId);

    TournamentPredictionDto saveTournamentPrediction(
            TournamentPredictionDto tournamentPredictionDto, String userId);

    TournamentPredictionDto getTournamentPredictionByUser(String userId);

    PredictionResponse getPredictionsForUserByMatches(String userId, List<String> matchIds);

    List<LeaderboardDTO> getLeaderboard(String location);
}
