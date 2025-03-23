package com.ipl.prediction.iplprediction.util;

import com.ipl.prediction.iplprediction.dto.IplUserDto;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.entity.Prediction;

public class MapperUtil {

    public static PredictionDto predictionToPredictionDto(Prediction prediction) {
        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setPredictionId(prediction.getPredictionId());
        predictionDto.setMatchId(prediction.getMatchId());
        predictionDto.setUserId(prediction.getUser().getUserId());
        predictionDto.setUser(iplUserToIplUserDto(prediction.getUser()));
        predictionDto.setTossPredicted(prediction.getTossPredicted());
        predictionDto.setFirstInnScorePredicted(prediction.getFirstInnScorePredicted());
        predictionDto.setTeamPredicted(prediction.getTeamPredicted());
        predictionDto.setMomPredicted(prediction.getMomPredicted());
        predictionDto.setMostRunsScorerPredicted(prediction.getMostRunsScorerPredicted());
        predictionDto.setMostWicketsTakerPredicted(prediction.getMostWicketsTakerPredicted());
        predictionDto.setPoints(prediction.getPoints() == null ? 0 : prediction.getPoints());
        return predictionDto;
    }

    private static IplUserDto iplUserToIplUserDto(IplUser iplUser) {
        IplUserDto iplUserDto = new IplUserDto();
        iplUserDto.setName(iplUser.getName());
        iplUserDto.setUserId(iplUserDto.getUserId());
        iplUserDto.setLocation(iplUser.getLocation());
        return iplUserDto;
    }
}
