package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.model.IplPlayer;
import com.ipl.prediction.iplprediction.request.PlayerRequest;
import com.ipl.prediction.iplprediction.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private CsvService csvService;

    @PostMapping
    public ResponseEntity<List<IplPlayer>> getPlayersByTeam(@RequestBody PlayerRequest request) throws IOException {
        List<IplPlayer> players = csvService.readPlayersFromCsv();
        Set<String> teamsSet = Set.copyOf(request.getTeams());

        List<IplPlayer> filteredPlayers = players.stream()
                .filter(player -> teamsSet.contains(player.getTeam()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredPlayers);
    }
}
