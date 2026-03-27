package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.dto.MatchResultDto;
import com.ipl.prediction.iplprediction.request.PredictionRequest;
import com.ipl.prediction.iplprediction.response.AdminResponse;
import com.ipl.prediction.iplprediction.service.AdminService;
import com.ipl.prediction.iplprediction.service.CricApiService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CricApiService cricApiService;

    @PersistenceContext
    private EntityManager entityManager;

    private static final java.util.Set<String> ALLOWED_TABLES = java.util.Set.of(
            "ipl_users", "ipl_teams", "ipl_players", "ipl_matches", "ipl_predictions", "tournament_predictions"
    );

    @PostMapping("/truncate")
    @Transactional
    public ResponseEntity<AdminResponse> truncateTable(@RequestBody Map<String, String> request) {
        AdminResponse response = new AdminResponse();
        String tableName = request.get("tableName");
        if (tableName == null || !ALLOWED_TABLES.contains(tableName.toLowerCase())) {
            response.setStatus(false);
            response.setMessage("Invalid table name. Allowed: " + ALLOWED_TABLES);
            return ResponseEntity.badRequest().body(response);
        }
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " CASCADE").executeUpdate();
        response.setStatus(true);
        response.setMessage("Table " + tableName + " truncated successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sync/results")
    public ResponseEntity<Map<String, Object>> syncResults() {
        return ResponseEntity.ok(cricApiService.syncMatchResults());
    }

    @GetMapping("/predictions/match")
    public ResponseEntity<AdminResponse> getPredictionsByMatch(
            @RequestParam String matchId) {
        AdminResponse response = new AdminResponse();
        response.setPredictions(adminService.getPredictionsByMatch(matchId));
        response.setStatus(true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/match/result")
    public ResponseEntity<AdminResponse> getMatchResult(
            @RequestParam String matchId) {
        AdminResponse response = new AdminResponse();
        response.setMatchResult(adminService.getMatchResult(matchId));
        response.setStatus(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/match/result")
    public ResponseEntity<AdminResponse> updateMatchResults(
            @RequestBody MatchResultDto matchResult) {
        AdminResponse response = adminService.updateMatchResults(matchResult);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/prediction/delete")
    public ResponseEntity<AdminResponse> deletePredictions(
            @RequestBody PredictionRequest request) {
        AdminResponse response = adminService.deletePredictions(request.getMatchIds());
        return ResponseEntity.ok(response);
    }
}
