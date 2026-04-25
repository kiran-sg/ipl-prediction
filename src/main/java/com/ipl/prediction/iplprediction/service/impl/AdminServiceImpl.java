package com.ipl.prediction.iplprediction.service.impl;

import com.ipl.prediction.iplprediction.dto.MatchResultDto;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.dto.TournamentPredictionDto;
import com.ipl.prediction.iplprediction.dto.TournamentResultDto;
import com.ipl.prediction.iplprediction.entity.IplPlayer;
import com.ipl.prediction.iplprediction.entity.IplTeam;
import com.ipl.prediction.iplprediction.entity.Prediction;
import com.ipl.prediction.iplprediction.entity.TournamentPrediction;
import com.ipl.prediction.iplprediction.entity.TournamentResult;
import com.ipl.prediction.iplprediction.repository.PredictionRepository;
import com.ipl.prediction.iplprediction.repository.PlayerRepository;
import com.ipl.prediction.iplprediction.repository.TeamRepository;
import com.ipl.prediction.iplprediction.repository.TournamentPredictionRepository;
import com.ipl.prediction.iplprediction.repository.TournamentResultRepository;
import com.ipl.prediction.iplprediction.repository.UserRepository;
import com.ipl.prediction.iplprediction.response.AdminResponse;
import com.ipl.prediction.iplprediction.service.AdminService;
import com.ipl.prediction.iplprediction.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private PredictionRepository predictionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TournamentPredictionRepository tournamentPredictionRepository;
    @Autowired
    private TournamentResultRepository tournamentResultRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Value("${tournament.prediction.points-per-question:5}")
    private int pointsPerQuestion;

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

    @Override
    public AdminResponse updateTournamentResults(TournamentResultDto dto) {
        AdminResponse response = new AdminResponse();

        // Save/update result (keep only one row)
        TournamentResult result = tournamentResultRepository.findAll().stream()
                .findFirst().orElse(new TournamentResult());
        result.setOrangeCapWinner(resolvePlayer(dto.getOrangeCapWinnerId()));
        result.setPurpleCapWinner(resolvePlayer(dto.getPurpleCapWinnerId()));
        result.setEmergingPlayerWinner(resolvePlayer(dto.getEmergingPlayerWinnerId()));
        result.setFairPlayTeamWinner(resolveTeam(dto.getFairPlayTeamWinnerId()));
        result.setMostFoursWinner(resolvePlayer(dto.getMostFoursWinnerId()));
        result.setMostSixesWinner(resolvePlayer(dto.getMostSixesWinnerId()));
        result.setMostDotBallsWinner(resolvePlayer(dto.getMostDotBallsWinnerId()));
        result.setBestBowlingFigWinner(resolvePlayer(dto.getBestBowlingFigWinnerId()));
        result.setPlayerOfTournamentWinner(resolvePlayer(dto.getPlayerOfTournamentWinnerId()));
        result.setUpdatedTime(LocalDateTime.now());
        tournamentResultRepository.save(result);

        // Score all user predictions
        List<TournamentPrediction> predictions = tournamentPredictionRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (TournamentPrediction p : predictions) {
            int points = 0;
            if (matchesPlayer(p.getOrangeCapPredicted(), result.getOrangeCapWinner())) points += pointsPerQuestion;
            if (matchesPlayer(p.getPurpleCapPredicted(), result.getPurpleCapWinner())) points += pointsPerQuestion;
            if (matchesPlayer(p.getEmergingPlayerPredicted(), result.getEmergingPlayerWinner())) points += pointsPerQuestion;
            if (matchesTeam(p.getFairPlayTeamPredicted(), result.getFairPlayTeamWinner())) points += pointsPerQuestion;
            if (matchesPlayer(p.getMostFoursPredicted(), result.getMostFoursWinner())) points += pointsPerQuestion;
            if (matchesPlayer(p.getMostSixesPredicted(), result.getMostSixesWinner())) points += pointsPerQuestion;
            if (matchesPlayer(p.getMostDotBallsPredicted(), result.getMostDotBallsWinner())) points += pointsPerQuestion;
            if (matchesPlayer(p.getBestBowlingFigPredicted(), result.getBestBowlingFigWinner())) points += pointsPerQuestion;
            if (matchesPlayer(p.getPlayerOfTournamentPredicted(), result.getPlayerOfTournamentWinner())) points += pointsPerQuestion;
            p.setPoints(points);
            p.setResultUpdatedTime(now);
        }
        tournamentPredictionRepository.saveAll(predictions);

        response.setStatus(true);
        response.setMessage("Tournament Results Saved");
        return response;
    }

    @Override
    public TournamentResultDto getTournamentResult() {
        return tournamentResultRepository.findAll().stream()
                .findFirst()
                .map(r -> {
                    TournamentResultDto dto = new TournamentResultDto();
                    dto.setOrangeCapWinnerId(r.getOrangeCapWinner() != null ? r.getOrangeCapWinner().getId() : null);
                    dto.setPurpleCapWinnerId(r.getPurpleCapWinner() != null ? r.getPurpleCapWinner().getId() : null);
                    dto.setEmergingPlayerWinnerId(r.getEmergingPlayerWinner() != null ? r.getEmergingPlayerWinner().getId() : null);
                    dto.setFairPlayTeamWinnerId(r.getFairPlayTeamWinner() != null ? r.getFairPlayTeamWinner().getId() : null);
                    dto.setMostFoursWinnerId(r.getMostFoursWinner() != null ? r.getMostFoursWinner().getId() : null);
                    dto.setMostSixesWinnerId(r.getMostSixesWinner() != null ? r.getMostSixesWinner().getId() : null);
                    dto.setMostDotBallsWinnerId(r.getMostDotBallsWinner() != null ? r.getMostDotBallsWinner().getId() : null);
                    dto.setBestBowlingFigWinnerId(r.getBestBowlingFigWinner() != null ? r.getBestBowlingFigWinner().getId() : null);
                    dto.setPlayerOfTournamentWinnerId(r.getPlayerOfTournamentWinner() != null ? r.getPlayerOfTournamentWinner().getId() : null);
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public List<TournamentPredictionDto> getAllTournamentPredictions() {
        return tournamentPredictionRepository.findAll().stream()
                .map(MapperUtil::tournamentPredictionToTournamentPredictionDto)
                .toList();
    }

    private IplPlayer resolvePlayer(Long id) {
        return id == null ? null : playerRepository.findById(id).orElse(null);
    }

    private IplTeam resolveTeam(Long id) {
        return id == null ? null : teamRepository.findById(id).orElse(null);
    }

    private boolean matchesPlayer(IplPlayer predicted, IplPlayer actual) {
        return predicted != null && actual != null && Objects.equals(predicted.getId(), actual.getId());
    }

    private boolean matchesTeam(IplTeam predicted, IplTeam actual) {
        return predicted != null && actual != null && Objects.equals(predicted.getId(), actual.getId());
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

        if (isValid(prediction.getTossPredicted()) && isValid(resultDto.getTossWon())
                && prediction.getTossPredicted().equals(resultDto.getTossWon())) {
            totalPointsWonByUser += 3;
        }
        if (isValid(prediction.getFirstInnScorePredicted()) && isValid(resultDto.getFirstInnScore())
                && prediction.getFirstInnScorePredicted().equals(resultDto.getFirstInnScore())) {
            totalPointsWonByUser += 3;
        }
        if (isValid(prediction.getTeamPredicted()) && isValid(resultDto.getTeamWon())
                && prediction.getTeamPredicted().equals(resultDto.getTeamWon())) {
            totalPointsWonByUser += 5;
        }
        if (isValid(prediction.getMostRunsScorerPredicted()) && isValid(resultDto.getMostRunsScorer())
                && prediction.getMostRunsScorerPredicted().equals(resultDto.getMostRunsScorer())) {
            totalPointsWonByUser += 3;
        }
        if (isValid(prediction.getMostWicketsTakerPredicted()) && isValid(resultDto.getMostWicketsTaker())
                && prediction.getMostWicketsTakerPredicted().equals(resultDto.getMostWicketsTaker())) {
            totalPointsWonByUser += 3;
        }
        if (isValid(prediction.getMomPredicted()) && isValid(resultDto.getPlayerOfTheMatch())
                && prediction.getMomPredicted().equals(resultDto.getPlayerOfTheMatch())) {
            totalPointsWonByUser += 3;
        }

        if (prediction.getSurgeUsed() != null && prediction.getSurgeUsed()) {
            totalPointsWonByUser *= 2;
        }

        return totalPointsWonByUser;
    }

    private boolean isValid(String value) {
        return value != null && !value.isEmpty() && !value.equalsIgnoreCase("NA") && !value.equalsIgnoreCase("N/A");
    }
}
