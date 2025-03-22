package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.dto.MatchResultDto;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.response.AdminResponse;

import java.util.List;

public interface AdminService {
    MatchResultDto getMatchResult(String matchId);

    List<PredictionDto> getPredictionsByMatch(String matchId);

    List<PredictionDto> getPredictionsByUser(String userId);

    AdminResponse updateMatchResults(MatchResultDto resultDto);
}
