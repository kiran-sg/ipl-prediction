package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.model.IplMatch;
import com.ipl.prediction.iplprediction.model.IplPlayer;
import com.ipl.prediction.iplprediction.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private CsvService csvService;

    @GetMapping
    public ResponseEntity<List<IplPlayer>> getPlayersByTeam(@RequestParam String team) throws IOException {
        List<IplPlayer> players = csvService.readPlayersFromCsv();
        List<IplPlayer> filteredPlayers = players.stream()
                .filter(player -> player.getTeam().equalsIgnoreCase(team))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredPlayers);
    }
}
