package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.dto.LeaderboardDTO;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.dto.TournamentPredictionDto;
import com.ipl.prediction.iplprediction.model.IplMatch;
import com.ipl.prediction.iplprediction.request.PredictionRequest;
import com.ipl.prediction.iplprediction.response.AdminResponse;
import com.ipl.prediction.iplprediction.response.PredictionResponse;
import com.ipl.prediction.iplprediction.service.CsvService;
import com.ipl.prediction.iplprediction.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.ipl.prediction.iplprediction.util.CommonUtil.isPredictionAllowed;
import static com.ipl.prediction.iplprediction.util.CommonUtil.isTournamentPredictionAllowed;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;
    @Autowired
    private CsvService csvService;

    @PostMapping
    public ResponseEntity<PredictionResponse> savePrediction(
            @RequestBody PredictionDto prediction) throws IOException {
        PredictionResponse predictionResponse = new PredictionResponse();
        IplMatch match = findMatchById(prediction.getMatchId());
        if (!isPredictionAllowed(match.getDateTime())) {
            predictionResponse.setStatus(false);
            predictionResponse.setMessage("Prediction is locked for this match");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(predictionResponse);
        }
        if (prediction.getUserId() == null) {
            predictionResponse.setStatus(false);
            predictionResponse.setInvalidUser(true);
            predictionResponse.setMessage("Login session expired. Please login.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(predictionResponse);
        }
        PredictionDto savedPrediction = predictionService.savePrediction(prediction, prediction.getUserId());
        predictionResponse.setMessage("Prediction saved successfully");
        predictionResponse.setStatus(true);
        predictionResponse.setPrediction(savedPrediction);
        return ResponseEntity.ok(predictionResponse);
    }

    @PostMapping("/match")
    public ResponseEntity<PredictionResponse> getPrediction(
            @RequestBody PredictionRequest request) {
        PredictionResponse predictionResponse = new PredictionResponse();
        PredictionDto prediction = predictionService.getPrediction(request.getUserId(), request.getMatchId());
        predictionResponse.setStatus(true);
        predictionResponse.setPrediction(prediction);
        return ResponseEntity.ok(predictionResponse);
    }

    @GetMapping
    public ResponseEntity<AdminResponse> getPredictionsByUser(
            @RequestParam String user) throws IOException {
        AdminResponse response = new AdminResponse();
        response.setPredictions(predictionService.getPredictionsByUser(user));
        response.setStatus(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/matches")
    public ResponseEntity<PredictionResponse> getPredictionsForUserByMatches(
            @RequestBody PredictionRequest request) {
        PredictionResponse response = predictionService
                .getPredictionsForUserByMatches(request.getUserId(), request.getMatchIds());
        response.setStatus(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tournament")
    public ResponseEntity<PredictionResponse> saveTournamentPrediction(
            @RequestBody TournamentPredictionDto tournamentPredictionDto) {
        PredictionResponse predictionResponse = new PredictionResponse();
        if (!isTournamentPredictionAllowed()) {
            predictionResponse.setStatus(false);
            predictionResponse.setMessage("Season Prediction is closed. You can't predict now.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(predictionResponse);
        }
        if (tournamentPredictionDto.getUserId() == null) {
            predictionResponse.setStatus(false);
            predictionResponse.setInvalidUser(true);
            predictionResponse.setMessage("Login session expired. Please login.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(predictionResponse);
        }
        TournamentPredictionDto savedPrediction = predictionService
                .saveTournamentPrediction(tournamentPredictionDto, tournamentPredictionDto.getUserId());
        predictionResponse.setMessage("Season Prediction saved successfully");
        predictionResponse.setStatus(true);
        predictionResponse.setTournamentPrediction(savedPrediction);
        return ResponseEntity.ok(predictionResponse);
    }

    @GetMapping("/tournament")
    public ResponseEntity<PredictionResponse> getTournamentPredictionByUser(
            @RequestParam String user) throws IOException {
        PredictionResponse response = new PredictionResponse();
        response.setTournamentPrediction(predictionService.getTournamentPredictionByUser(user));
        response.setStatus(true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboard(
            @RequestParam String location) {
        List<LeaderboardDTO> leaderboard = predictionService.getLeaderboard(location);
        return ResponseEntity.ok(leaderboard);
    }

    private IplMatch findMatchById(String matchId) throws IOException {
        return csvService.readMatchesFromCsv().stream()
                .filter(match -> match.getMatchNo().equals(matchId))
                .findFirst()
                .orElse(null);
    }
}
