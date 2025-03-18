package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.model.IplMatch;
import com.ipl.prediction.iplprediction.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private CsvService csvService;

    @GetMapping
    public ResponseEntity<List<IplMatch>> getAllMatches() throws IOException {
        List<IplMatch> iplMatches = csvService.readMatchesFromCsv();
        return ResponseEntity.ok(iplMatches);
    }
}
