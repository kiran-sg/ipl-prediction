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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM");

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
    public PredictionDto savePrediction(PredictionDto predictionDto, String userId) {
        IplUser user = userRepository.findByUserId(predictionDto.getUserId());
        Optional<Prediction> optionalPrediction = predictionRepository
                .findByUserAndMatchId(user, predictionDto.getMatchId());

        if (optionalPrediction.isPresent()) {
            System.out.println("Updating existing prediction for user: "
                    + userId + " and match id: " + predictionDto.getMatchId());
            Prediction existingPrediction = optionalPrediction.get();
            updatePrediction(existingPrediction, predictionDto);
            Prediction updatedPrediction = predictionRepository.save(existingPrediction);
            return predictionToPredictionDto(updatedPrediction);
        } else {
            System.out.println("No existing prediction found for the given user: "
                    + user + " and match ID: " + predictionDto.getMatchId());
            Prediction newPrediction = new Prediction();
            updatePrediction(newPrediction, predictionDto);
            newPrediction.setMatchId(predictionDto.getMatchId());
            newPrediction.setUser(user);
            Prediction savedPrediction = predictionRepository.save(newPrediction);
            return predictionToPredictionDto(savedPrediction);
        }
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

        predictionDtoList.forEach(predictionDto -> matches.stream()
                .filter(match -> Objects.equals(predictionDto.getMatchId(), match.getMatchNo()))
                .findFirst()
                .ifPresent(match -> {
                    predictionDto.setMatchId(match.getMatchNo());
                    predictionDto.setMatch(match.getHome() + " VS " + match.getAway());
                    predictionDto.setMatchDateTime(match.getDateTime());
                    String formattedDate = LocalDateTime.parse(
                            match.getDateTime(), inputFormatter).format(outputFormatter);
                    predictionDto.setMatchDate(formattedDate);
                }));

        return predictionDtoList.stream()
                .sorted(Comparator.comparing(dto ->
                        LocalDateTime.parse(dto.getMatchDateTime(), inputFormatter),
                        Comparator.reverseOrder()))
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
