package com.ipl.prediction.iplprediction.util;

import com.ipl.prediction.iplprediction.dto.IplUserDto;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.dto.TournamentPredictionDto;
import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.entity.Prediction;
import com.ipl.prediction.iplprediction.entity.TournamentPrediction;

import java.sql.Timestamp;

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

    public static void updatePrediction(Prediction prediction, PredictionDto predictionDto) {
        prediction.setTossPredicted(predictionDto.getTossPredicted());
        prediction.setFirstInnScorePredicted(predictionDto.getFirstInnScorePredicted());
        prediction.setTeamPredicted(predictionDto.getTeamPredicted());
        prediction.setMomPredicted(predictionDto.getMomPredicted());
        prediction.setMostRunsScorerPredicted(predictionDto.getMostRunsScorerPredicted());
        prediction.setMostWicketsTakerPredicted(predictionDto.getMostWicketsTakerPredicted());
        prediction.setPredictionTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
    }

    public static TournamentPredictionDto tournamentPredictionToTournamentPredictionDto(
            TournamentPrediction tournamentPrediction) {
        TournamentPredictionDto tournamentPredictionDto = new TournamentPredictionDto();
        tournamentPredictionDto.setPredictionId(tournamentPrediction.getPredictionId());
        tournamentPredictionDto.setUserId(tournamentPrediction.getUser().getUserId());
        tournamentPredictionDto.setUser(iplUserToIplUserDto(tournamentPrediction.getUser()));
        tournamentPredictionDto.setOrangeCapPredicted(tournamentPrediction.getOrangeCapPredicted());
        tournamentPredictionDto.setPurpleCapPredicted(tournamentPrediction.getPurpleCapPredicted());
        tournamentPredictionDto.setEmergingPlayerPredicted(tournamentPrediction.getEmergingPlayerPredicted());
        tournamentPredictionDto.setFairPlayTeamPredicted(tournamentPrediction.getFairPlayTeamPredicted());
        tournamentPredictionDto.setMostFoursPredicted(tournamentPrediction.getMostFoursPredicted());
        tournamentPredictionDto.setMostSixesPredicted(tournamentPrediction.getMostSixesPredicted());
        tournamentPredictionDto.setMostDotBallsPredicted(tournamentPrediction.getMostDotBallsPredicted());
        tournamentPredictionDto.setBestBowlingFigPredicted(tournamentPrediction.getBestBowlingFigPredicted());
        tournamentPredictionDto.setPredictionTime(tournamentPrediction.getPredictionTime());
        tournamentPredictionDto.setPoints(tournamentPrediction.getPoints() == null ? 0 : tournamentPrediction.getPoints());
        return tournamentPredictionDto;
    }

    public static void updateTournamentPrediction(TournamentPrediction tournamentPrediction,
                                                  TournamentPredictionDto tournamentPredictionDto) {
        tournamentPrediction.setOrangeCapPredicted(tournamentPredictionDto.getOrangeCapPredicted());
        tournamentPrediction.setPurpleCapPredicted(tournamentPredictionDto.getPurpleCapPredicted());
        tournamentPrediction.setEmergingPlayerPredicted(tournamentPredictionDto.getEmergingPlayerPredicted());
        tournamentPrediction.setFairPlayTeamPredicted(tournamentPredictionDto.getFairPlayTeamPredicted());
        tournamentPrediction.setMostFoursPredicted(tournamentPredictionDto.getMostFoursPredicted());
        tournamentPrediction.setMostSixesPredicted(tournamentPredictionDto.getMostSixesPredicted());
        tournamentPrediction.setMostDotBallsPredicted(tournamentPredictionDto.getMostDotBallsPredicted());
        tournamentPrediction.setBestBowlingFigPredicted(tournamentPredictionDto.getBestBowlingFigPredicted());
        tournamentPrediction.setPredictionTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
    }

    private static IplUserDto iplUserToIplUserDto(IplUser iplUser) {
        IplUserDto iplUserDto = new IplUserDto();
        iplUserDto.setName(iplUser.getName());
        iplUserDto.setUserId(iplUserDto.getUserId());
        iplUserDto.setLocation(iplUser.getLocation());
        return iplUserDto;
    }
}
