package com.ipl.prediction.iplprediction.service.impl;

import com.ipl.prediction.iplprediction.dto.LeaderboardDTO;
import com.ipl.prediction.iplprediction.entity.Prediction;
import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.repository.PredictionRepository;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.repository.UserRepository;
import com.ipl.prediction.iplprediction.service.PredictionService;
import com.ipl.prediction.iplprediction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {

    @Autowired
    private PredictionRepository predictionRepository;
    @Autowired
    private UserRepository userRepository;

    private Prediction predictionDtoToPrediction(PredictionDto predictionDto) {
        Prediction prediction = new Prediction();
        prediction.setMatchId(predictionDto.getMatchId());
        prediction.setTossPredicted(predictionDto.getTossPredicted());
        prediction.setTeamPredicted(predictionDto.getTeamPredicted());
        prediction.setMomPredicted(predictionDto.getMomPredicted());
        prediction.setMostRunsScorerPredicted(predictionDto.getMostRunsScorerPredicted());
        prediction.setMostWicketsTakerPredicted(predictionDto.getMostWicketsTakerPredicted());
        prediction.setPredictionTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        return prediction;
    }

    private PredictionDto predictionToPredictionDto(Prediction prediction) {
        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setPredictionId(prediction.getPredictionId());
        predictionDto.setMatchId(prediction.getMatchId());
        predictionDto.setUserId(prediction.getUser().getUserId());
        predictionDto.setTossPredicted(prediction.getTossPredicted());
        predictionDto.setTeamPredicted(prediction.getTeamPredicted());
        predictionDto.setMomPredicted(prediction.getMomPredicted());
        predictionDto.setMostRunsScorerPredicted(prediction.getMostRunsScorerPredicted());
        predictionDto.setMostWicketsTakerPredicted(prediction.getMostWicketsTakerPredicted());
        return predictionDto;
    }

    @Override
    public PredictionDto savePrediction(PredictionDto predictionDto) {
        Prediction prediction = predictionDtoToPrediction(predictionDto);
        IplUser user = userRepository.findByUserId(predictionDto.getUserId());
        prediction.setUser(user);
        prediction = predictionRepository.save(prediction);
        return predictionToPredictionDto(prediction);
    }

    @Override
    public PredictionDto editPrediction(Long predictionId, PredictionDto updatedPrediction) {
        return predictionRepository.findById(predictionId)
                .map(prediction -> {
                    Prediction predictionEntity = predictionRepository
                            .save(predictionDtoToPrediction(updatedPrediction));
                    return predictionToPredictionDto(predictionEntity);
                })
                .orElseThrow(() -> new RuntimeException("Prediction not found with id: " + predictionId));
    }

    @Override
    public List<LeaderboardDTO> getLeaderboard() {
        List<Object[]> results = predictionRepository.getLeaderboard();

        return IntStream.range(0, results.size())
                .mapToObj(index -> {
                    Object[] result = results.get(index);
                    return new LeaderboardDTO(
                            ((IplUser) result[0]).getUserId(), // User ID
                            ((IplUser) result[0]).getLocation(), // User Location
                            ((Long) result[1]).intValue(), // Total Points
                            index + 1 // Position (rank)
                    );
                })
                .collect(Collectors.toList());
    }
}
