package com.ipl.prediction.iplprediction.util;

import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.entity.Prediction;

public class MapperUtil {

    public static PredictionDto predictionToPredictionDto(Prediction prediction) {
        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setPredictionId(prediction.getPredictionId());
        predictionDto.setMatchId(prediction.getMatchId());
        predictionDto.setUserId(prediction.getUser().getUserId());
        predictionDto.setTossPredicted(prediction.getTossPredicted());
        predictionDto.setFirstInnScorePredicted(prediction.getFirstInnScorePredicted());
        predictionDto.setTeamPredicted(prediction.getTeamPredicted());
        predictionDto.setMomPredicted(prediction.getMomPredicted());
        predictionDto.setMostRunsScorerPredicted(prediction.getMostRunsScorerPredicted());
        predictionDto.setMostWicketsTakerPredicted(prediction.getMostWicketsTakerPredicted());
        return predictionDto;
    }
}
