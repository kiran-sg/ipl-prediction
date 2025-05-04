package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.dto.LeaderboardDTO;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.dto.TournamentPredictionDto;

import java.io.IOException;
import java.util.List;

public interface PredictionService {
    PredictionDto getPrediction(String userId, String matchId);

    PredictionDto savePrediction(PredictionDto predictionDto, String userId);

    List<PredictionDto> getPredictionsByUser(String userId) throws IOException;

    TournamentPredictionDto saveTournamentPrediction(
            TournamentPredictionDto tournamentPredictionDto, String userId);

    TournamentPredictionDto getTournamentPredictionByUser(String userId) throws IOException;

    List<LeaderboardDTO> getLeaderboard(String location);
}
