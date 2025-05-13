package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.dto.MatchResultDto;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.response.AdminResponse;

import java.util.List;

public interface AdminService {
    MatchResultDto getMatchResult(String matchId);

    List<PredictionDto> getPredictionsByMatch(String matchId);

    AdminResponse updateMatchResults(MatchResultDto resultDto);

    AdminResponse deletePredictions(List<String> matchId);
}
