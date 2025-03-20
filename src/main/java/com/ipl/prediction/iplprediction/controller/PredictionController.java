package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.dto.LeaderboardDTO;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.request.PredictionRequest;
import com.ipl.prediction.iplprediction.response.PredictionResponse;
import com.ipl.prediction.iplprediction.service.PredictionService;
import com.ipl.prediction.iplprediction.util.EncryptionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping
    public ResponseEntity<PredictionResponse> savePrediction(
            @RequestBody PredictionDto prediction, HttpSession httpSession) {
        PredictionResponse predictionResponse = new PredictionResponse();
        /*String userId = (String) httpSession.getAttribute("userId");
        if (userId == null) {
            predictionResponse.setStatus(false);
            predictionResponse.setInvalidUser(true);
            predictionResponse.setMessage("Login session expired. Please log in.");
            return ResponseEntity.ok(predictionResponse);
        }*/
        PredictionDto savedPrediction = predictionService.savePrediction(prediction);
        predictionResponse.setMessage("Prediction saved successfully");
        predictionResponse.setStatus(true);
        predictionResponse.setPrediction(savedPrediction);
        return ResponseEntity.ok(predictionResponse);
    }

    @PostMapping("/match")
    public ResponseEntity<PredictionResponse> getPrediction(
            @RequestBody PredictionRequest request) {
        //String decryptedUserId = EncryptionUtil.decrypt(request.getUserId());
        PredictionResponse predictionResponse = new PredictionResponse();
        /*String userId = (String) httpSession.getAttribute("userId");
        if (userId == null) {
            PredictionResponse response = new PredictionResponse();
            response.setStatus(false);
            response.setInvalidUser(true);
            response.setMessage("User ID not found. Please log in.");
            return ResponseEntity.ok(response);
        }*/
        PredictionDto prediction = predictionService.getPrediction(request.getUserId(), request.getMatchId());
        predictionResponse.setStatus(true);
        predictionResponse.setPrediction(prediction);
        return ResponseEntity.ok(predictionResponse);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboard(
            @RequestParam String location) {
        List<LeaderboardDTO> leaderboard = predictionService.getLeaderboard(location);
        return ResponseEntity.ok(leaderboard);
    }
}
