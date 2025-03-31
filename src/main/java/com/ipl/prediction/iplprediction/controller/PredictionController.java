package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.dto.LeaderboardDTO;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.request.PredictionRequest;
import com.ipl.prediction.iplprediction.response.AdminResponse;
import com.ipl.prediction.iplprediction.response.PredictionResponse;
import com.ipl.prediction.iplprediction.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping
    public ResponseEntity<PredictionResponse> savePrediction(
            @RequestBody PredictionDto prediction) {
        PredictionResponse predictionResponse = new PredictionResponse();
        if (prediction.getUserId() == null) {
            predictionResponse.setStatus(false);
            predictionResponse.setInvalidUser(true);
            predictionResponse.setMessage("Login session expired. Please log in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(predictionResponse);
        }
        PredictionDto savedPrediction = predictionService.savePrediction(prediction);
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

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboard(
            @RequestParam String location) {
        List<LeaderboardDTO> leaderboard = predictionService.getLeaderboard(location);
        return ResponseEntity.ok(leaderboard);
    }
}
