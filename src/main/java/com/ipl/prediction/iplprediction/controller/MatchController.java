package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.entity.IplMatch;
import com.ipl.prediction.iplprediction.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchRepository matchRepository;

    @GetMapping
    public ResponseEntity<List<IplMatch>> getAllMatches() {
        return ResponseEntity.ok(matchRepository.findAllOrderByDateTime());
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncMatches(@RequestBody List<IplMatch> matches) {
        int created = 0, updated = 0;
        for (IplMatch incoming : matches) {
            IplMatch existing = matchRepository.findByMatchNo(incoming.getMatchNo()).orElse(null);
            if (existing != null) {
                existing.setDateTime(incoming.getDateTime());
                existing.setHome(incoming.getHome());
                existing.setAway(incoming.getAway());
                matchRepository.save(existing);
                updated++;
            } else {
                matchRepository.save(incoming);
                created++;
            }
        }
        return ResponseEntity.ok(Map.of("created", created, "updated", updated, "total", matches.size()));
    }
}
