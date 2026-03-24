package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.entity.IplPlayer;
import com.ipl.prediction.iplprediction.repository.PlayerRepository;
import com.ipl.prediction.iplprediction.request.PlayerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @PostMapping
    public ResponseEntity<List<IplPlayer>> getPlayersByTeam(@RequestBody PlayerRequest request) {
        if (request.getTeams() != null && !request.getTeams().isEmpty()) {
            return ResponseEntity.ok(playerRepository.findByTeamIn(request.getTeams()));
        }
        return ResponseEntity.ok(playerRepository.findAll());
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncPlayers(@RequestBody List<IplPlayer> players) {
        int created = 0;
        int updated = 0;
        for (IplPlayer player : players) {
            List<IplPlayer> existing = playerRepository.findByPlayerNameAndTeam(
                    player.getPlayerName(), player.getTeam());
            if (existing.isEmpty()) {
                playerRepository.save(player);
                created++;
            } else if (player.getImageUrl() != null && !player.getImageUrl().isBlank()) {
                IplPlayer ex = existing.get(0);
                ex.setImageUrl(player.getImageUrl());
                playerRepository.save(ex);
                updated++;
            }
        }
        return ResponseEntity.ok(Map.of("created", created, "updated", updated, "total", players.size()));
    }
}
