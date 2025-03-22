package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.dto.MatchResultDto;
import com.ipl.prediction.iplprediction.response.AdminResponse;
import com.ipl.prediction.iplprediction.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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

    @GetMapping("/predictions/user")
    public ResponseEntity<AdminResponse> getPredictionsByUser(
            @RequestParam String user) {
        AdminResponse response = new AdminResponse();
        response.setPredictions(adminService.getPredictionsByUser(user));
        response.setStatus(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/match/result")
    public ResponseEntity<AdminResponse> updateMatchResults(
            @RequestBody MatchResultDto matchResult) {
        AdminResponse response = adminService.updateMatchResults(matchResult);
        return ResponseEntity.ok(response);
    }
}
