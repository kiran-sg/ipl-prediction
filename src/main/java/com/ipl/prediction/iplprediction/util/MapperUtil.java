package com.ipl.prediction.iplprediction.util;

import com.ipl.prediction.iplprediction.dto.IplUserDto;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.dto.TournamentPredictionDto;
import com.ipl.prediction.iplprediction.entity.*;

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
        predictionDto.setSurgeUsed(prediction.getSurgeUsed() != null && prediction.getSurgeUsed());
        predictionDto.setTossWon(prediction.getTossWon());
        predictionDto.setFirstInnScore(prediction.getFirstInnScore());
        predictionDto.setTeamWon(prediction.getTeamWon());
        predictionDto.setMom(prediction.getMom());
        predictionDto.setMostRunsScorer(prediction.getMostRunsScorer());
        predictionDto.setMostWicketsTaker(prediction.getMostWicketsTaker());
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
            TournamentPrediction tp) {
        TournamentPredictionDto dto = new TournamentPredictionDto();
        dto.setPredictionId(tp.getPredictionId());
        dto.setUserId(tp.getUser().getUserId());
        dto.setUser(iplUserToIplUserDto(tp.getUser()));
        dto.setOrangeCapPredictedId(tp.getOrangeCapPredicted() != null ? tp.getOrangeCapPredicted().getId() : null);
        dto.setPurpleCapPredictedId(tp.getPurpleCapPredicted() != null ? tp.getPurpleCapPredicted().getId() : null);
        dto.setEmergingPlayerPredictedId(tp.getEmergingPlayerPredicted() != null ? tp.getEmergingPlayerPredicted().getId() : null);
        dto.setFairPlayTeamPredictedId(tp.getFairPlayTeamPredicted() != null ? tp.getFairPlayTeamPredicted().getId() : null);
        dto.setMostFoursPredictedId(tp.getMostFoursPredicted() != null ? tp.getMostFoursPredicted().getId() : null);
        dto.setMostSixesPredictedId(tp.getMostSixesPredicted() != null ? tp.getMostSixesPredicted().getId() : null);
        dto.setMostDotBallsPredictedId(tp.getMostDotBallsPredicted() != null ? tp.getMostDotBallsPredicted().getId() : null);
        dto.setBestBowlingFigPredictedId(tp.getBestBowlingFigPredicted() != null ? tp.getBestBowlingFigPredicted().getId() : null);
        dto.setPlayerOfTournamentPredictedId(tp.getPlayerOfTournamentPredicted() != null ? tp.getPlayerOfTournamentPredicted().getId() : null);
        dto.setPredictionTime(tp.getPredictionTime());
        dto.setPoints(tp.getPoints() == null ? 0 : tp.getPoints());
        return dto;
    }

    public static void updateTournamentPrediction(TournamentPrediction tp,
                                                  IplPlayer orangeCap, IplPlayer purpleCap,
                                                  IplPlayer emergingPlayer, IplTeam fairPlayTeam,
                                                  IplPlayer mostFours, IplPlayer mostSixes,
                                                  IplPlayer mostDotBalls, IplPlayer bestBowlingFig,
                                                  IplPlayer playerOfTournament) {
        tp.setOrangeCapPredicted(orangeCap);
        tp.setPurpleCapPredicted(purpleCap);
        tp.setEmergingPlayerPredicted(emergingPlayer);
        tp.setFairPlayTeamPredicted(fairPlayTeam);
        tp.setMostFoursPredicted(mostFours);
        tp.setMostSixesPredicted(mostSixes);
        tp.setMostDotBallsPredicted(mostDotBalls);
        tp.setBestBowlingFigPredicted(bestBowlingFig);
        tp.setPlayerOfTournamentPredicted(playerOfTournament);
        tp.setPredictionTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
    }

    private static IplUserDto iplUserToIplUserDto(IplUser iplUser) {
        IplUserDto iplUserDto = new IplUserDto();
        iplUserDto.setName(iplUser.getName());
        iplUserDto.setUserId(iplUserDto.getUserId());
        iplUserDto.setLocation(iplUser.getLocation());
        iplUserDto.setSurgesRemaining(iplUser.getSurgesRemaining());
        return iplUserDto;
    }
}
