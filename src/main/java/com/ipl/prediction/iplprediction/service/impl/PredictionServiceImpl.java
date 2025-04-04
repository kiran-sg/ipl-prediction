package com.ipl.prediction.iplprediction.service.impl;

import com.ipl.prediction.iplprediction.dto.LeaderboardDTO;
import com.ipl.prediction.iplprediction.entity.Prediction;
import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.model.IplMatch;
import com.ipl.prediction.iplprediction.repository.PredictionRepository;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.repository.UserRepository;
import com.ipl.prediction.iplprediction.service.CsvService;
import com.ipl.prediction.iplprediction.service.PredictionService;
import com.ipl.prediction.iplprediction.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.ipl.prediction.iplprediction.util.MapperUtil.predictionToPredictionDto;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {

    @Autowired
    private PredictionRepository predictionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CsvService csvService;

    private void updatePrediction(Prediction prediction, PredictionDto predictionDto) {
        prediction.setTossPredicted(predictionDto.getTossPredicted());
        prediction.setFirstInnScorePredicted(predictionDto.getFirstInnScorePredicted());
        prediction.setTeamPredicted(predictionDto.getTeamPredicted());
        prediction.setMomPredicted(predictionDto.getMomPredicted());
        prediction.setMostRunsScorerPredicted(predictionDto.getMostRunsScorerPredicted());
        prediction.setMostWicketsTakerPredicted(predictionDto.getMostWicketsTakerPredicted());
        prediction.setPredictionTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
    }

    @Override
    public PredictionDto getPrediction(String userId, String matchId) {
        IplUser user = userRepository.findByUserId(userId);
        Optional<Prediction> prediction = predictionRepository.findByUserAndMatchId(user, matchId);
        return prediction.map(MapperUtil::predictionToPredictionDto)
                .orElse(null);

    }

    @Override
    public PredictionDto savePrediction(PredictionDto predictionDto) {
        Prediction prediction = new Prediction();
        if (null != predictionDto.getPredictionId() && !predictionDto.getPredictionId().equals(0L)) {
            Optional<Prediction> optionalPrediction = predictionRepository.findById(predictionDto.getPredictionId());
            if (optionalPrediction.isPresent()) {
                prediction = optionalPrediction.get();
                updatePrediction(prediction, predictionDto);
            }
        } else {
            updatePrediction(prediction, predictionDto);
            prediction.setMatchId(predictionDto.getMatchId());
            IplUser user = userRepository.findByUserId(predictionDto.getUserId());
            prediction.setUser(user);
        }
        prediction = predictionRepository.save(prediction);
        return predictionToPredictionDto(prediction);
    }

    @Override
    public List<PredictionDto> getPredictionsByUser(String userId) throws IOException {
        List<PredictionDto> predictionDtoList = new ArrayList<>();
        IplUser user = userRepository.findByUserId(userId);
        Optional<List<Prediction>> predictions = predictionRepository.findAllByUser(user);
        List<IplMatch> matches = csvService.readMatchesFromCsv();
        predictions.ifPresent(predictionList ->
                predictionList.forEach(prediction ->
                        predictionDtoList.add(MapperUtil.predictionToPredictionDto(prediction))));

        predictionDtoList.forEach(a -> matches.stream()
                .filter(b -> Objects.equals(a.getMatchId(), b.getMatchNo()))
                .findFirst()
                .ifPresent(b -> {
                    a.setMatch(b.getHome() + " VS " + b.getAway());
                }));

        return predictionDtoList.stream()
                .sorted(Comparator.comparingInt(dto -> Integer.parseInt(dto.getMatchId())))
                .toList();
    }

    @Override
    public List<LeaderboardDTO> getLeaderboard(String location) {
        List<Object[]> results = predictionRepository.getLeaderboardByLocation(location);

        List<LeaderboardDTO> leaderboard = new ArrayList<>();
        int rank = 1; // Initial rank
        int position = 1; // Absolute position counter

        for (int i = 0; i < results.size(); i++) {
            Object[] result = results.get(i);
            IplUser user = (IplUser) result[0];
            int totalPoints = ((Long) result[1]).intValue();

            // Check if it's not the first entry and points are different from the previous player
            if (i > 0 && totalPoints != ((Long) results.get(i - 1)[1]).intValue()) {
                rank = position; // Update rank to the current position
            }

            leaderboard.add(new LeaderboardDTO(
                    user.getUserId(),
                    user.getName(),
                    user.getLocation(),
                    totalPoints,
                    rank
            ));

            position++; // Always increment absolute position
        }

        return leaderboard.stream()
                .filter(data -> data.getTotalPoints() != 0)
                .collect(Collectors.toList());
    }
}
