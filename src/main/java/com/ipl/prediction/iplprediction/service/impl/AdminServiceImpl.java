package com.ipl.prediction.iplprediction.service.impl;

import com.ipl.prediction.iplprediction.dto.MatchResultDto;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.entity.Prediction;
import com.ipl.prediction.iplprediction.repository.PredictionRepository;
import com.ipl.prediction.iplprediction.repository.UserRepository;
import com.ipl.prediction.iplprediction.response.AdminResponse;
import com.ipl.prediction.iplprediction.service.AdminService;
import com.ipl.prediction.iplprediction.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private PredictionRepository predictionRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public MatchResultDto getMatchResult(String matchId) {
        Optional<List<Prediction>> predictions = predictionRepository.findAllByMatchId(matchId);
        return predictions
                .flatMap(predictionList -> predictionList.stream()
                        .filter(prediction -> prediction.getTeamWon() != null)
                        .map(prediction -> {
                            MatchResultDto matchResult = new MatchResultDto();
                            matchResult.setMatchId(prediction.getMatchId());
                            matchResult.setTossWon(prediction.getTossWon());
                            matchResult.setFirstInnScore(prediction.getFirstInnScore());
                            matchResult.setTeamWon(prediction.getTeamWon());
                            matchResult.setMostRunsScorer(prediction.getMostRunsScorer());
                            matchResult.setMostWicketsTaker(prediction.getMostWicketsTaker());
                            matchResult.setPlayerOfTheMatch(prediction.getMom());
                            return matchResult;
                        })
                        .findFirst())
                .orElse(null);
    }

    @Override
    public List<PredictionDto> getPredictionsByMatch(String matchId) {
        List<PredictionDto> predictionDtoList = new ArrayList<>();
        Optional<List<Prediction>> predictions = predictionRepository.findAllByMatchId(matchId);
        predictions.ifPresent(predictionList -> predictionList.forEach(prediction -> predictionDtoList.add(MapperUtil.predictionToPredictionDto(prediction))));
        return predictionDtoList;
    }

    @Override
    public AdminResponse updateMatchResults(MatchResultDto resultDto) {
        AdminResponse response = new AdminResponse();
        Optional<List<Prediction>> predictions = predictionRepository
                .findAllByMatchId(resultDto.getMatchId());
        predictions.ifPresent(predictionList -> predictionList.forEach(prediction -> updatePredictionResultsForMatch(prediction, resultDto)));
        predictions.ifPresent(predictionList -> predictionRepository.saveAll(predictionList));
        response.setMessage("Results updated for Match "
                + resultDto.getMatchId() + " : " + resultDto.getMatch());
        response.setStatus(true);
        return response;
    }

    @Override
    public AdminResponse deletePredictions(List<String> matchIds) {
        AdminResponse response = new AdminResponse();
        Optional<List<Prediction>> predictions = predictionRepository.findAllByMatchIdIn(matchIds);
        if (predictions.isPresent()) {
            List<Prediction> predictionList = predictions.get();
            predictionRepository.deleteAll(predictionList);
            response.setStatus(true);
            response.setMessage("Predictions deleted successfully for match: " + matchIds);
        }
        return response;
    }

    private void updatePredictionResultsForMatch(Prediction prediction,
                                              MatchResultDto resultDto) {
        prediction.setTossWon(resultDto.getTossWon());
        prediction.setFirstInnScore(resultDto.getFirstInnScore());
        prediction.setTeamWon(resultDto.getTeamWon());
        prediction.setMostRunsScorer(resultDto.getMostRunsScorer());
        prediction.setMostWicketsTaker(resultDto.getMostWicketsTaker());
        prediction.setMom(resultDto.getPlayerOfTheMatch());
        prediction.setPoints(totalPointsWonByUser(prediction, resultDto));
        prediction.setResultUpdatedTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
    }

    private int totalPointsWonByUser(Prediction prediction, MatchResultDto resultDto) {
        int totalPointsWonByUser = 0;

        if (prediction.getTossPredicted().equals(resultDto.getTossWon())) {
            totalPointsWonByUser += 3;
        }
        if (prediction.getFirstInnScorePredicted().equals(resultDto.getFirstInnScore())) {
            totalPointsWonByUser += 3;
        }
        if (prediction.getTeamPredicted().equals(resultDto.getTeamWon())) {
            totalPointsWonByUser += 5;
        }
        if (prediction.getMostRunsScorerPredicted().equals(resultDto.getMostRunsScorer())) {
            totalPointsWonByUser += 3;
        }
        if (prediction.getMostWicketsTakerPredicted().equals(resultDto.getMostWicketsTaker())) {
            totalPointsWonByUser += 3;
        }
        if (prediction.getMomPredicted().equals(resultDto.getPlayerOfTheMatch())) {
            totalPointsWonByUser += 3;
        }
        return totalPointsWonByUser;
    }
}
