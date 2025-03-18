package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.dto.LeaderboardDTO;
import com.ipl.prediction.iplprediction.dto.PredictionDto;
import com.ipl.prediction.iplprediction.service.PredictionService;
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
    public ResponseEntity<PredictionDto> savePrediction(@RequestBody PredictionDto prediction) {
        PredictionDto savedPrediction = predictionService.savePrediction(prediction);
        return ResponseEntity.ok(savedPrediction);
    }

    @PutMapping("/{predictionId}")
    public ResponseEntity<PredictionDto> editPrediction(
            @PathVariable Long predictionId,
            @RequestBody PredictionDto updatedPrediction) {
        PredictionDto editedPrediction = predictionService.editPrediction(predictionId, updatedPrediction);
        return ResponseEntity.ok(editedPrediction);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboard() {
        List<LeaderboardDTO> leaderboard = predictionService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
}
