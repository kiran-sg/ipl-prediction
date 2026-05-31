package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.dto.MatchResultDto;
import com.ipl.prediction.iplprediction.dto.TournamentResultDto;
import com.ipl.prediction.iplprediction.entity.IplMatch;
import com.ipl.prediction.iplprediction.entity.IplPlayer;
import com.ipl.prediction.iplprediction.entity.IplTeam;
import com.ipl.prediction.iplprediction.repository.MatchRepository;
import com.ipl.prediction.iplprediction.repository.PlayerRepository;
import com.ipl.prediction.iplprediction.repository.TeamRepository;
import com.ipl.prediction.iplprediction.request.PredictionRequest;
import com.ipl.prediction.iplprediction.response.AdminResponse;
import com.ipl.prediction.iplprediction.service.AdminService;
import com.ipl.prediction.iplprediction.service.CricApiService;
import static com.ipl.prediction.iplprediction.util.CommonUtil.isTournamentResultUpdateAllowed;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CricApiService cricApiService;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

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

    @PostMapping("/tournament/result")
    public ResponseEntity<AdminResponse> updateTournamentResults(
            @RequestBody TournamentResultDto resultDto) {
        if (!isTournamentResultUpdateAllowed()) {
            AdminResponse response = new AdminResponse();
            response.setStatus(false);
            response.setMessage("Tournament result update is not allowed until the tournament ends.");
            return ResponseEntity.badRequest().body(response);
        }
        AdminResponse response = adminService.updateTournamentResults(resultDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tournament/result")
    public ResponseEntity<TournamentResultDto> getTournamentResult() {
        return ResponseEntity.ok(adminService.getTournamentResult());
    }

    @GetMapping("/tournament/predictions")
    public ResponseEntity<AdminResponse> getAllTournamentPredictions() {
        AdminResponse response = new AdminResponse();
        response.setTournamentPredictions(adminService.getAllTournamentPredictions());
        response.setStatus(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    @Transactional
    public ResponseEntity<AdminResponse> importData(@RequestParam("file") MultipartFile file) {
        AdminResponse response = new AdminResponse();
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            int teamsCount = 0, playersCount = 0, matchesCount = 0;

            Sheet teamsSheet = workbook.getSheet("Teams");
            if (teamsSheet != null) {
                List<IplTeam> teams = new ArrayList<>();
                for (Row row : teamsSheet) {
                    if (row.getRowNum() == 0) continue;
                    IplTeam t = new IplTeam();
                    t.setShortName(cellStr(row, 1));
                    t.setTeamName(cellStr(row, 2));
                    t.setLogoUrl(cellStr(row, 3));
                    teams.add(t);
                }
                teamRepository.saveAll(teams);
                teamsCount = teams.size();
            }

            Sheet playersSheet = workbook.getSheet("Players");
            if (playersSheet != null) {
                List<IplPlayer> players = new ArrayList<>();
                for (Row row : playersSheet) {
                    if (row.getRowNum() == 0) continue;
                    IplPlayer p = new IplPlayer();
                    p.setPlayerNo(cellStr(row, 1));
                    p.setPlayerName(cellStr(row, 2));
                    p.setCategory(cellStr(row, 3));
                    p.setTeam(cellStr(row, 4));
                    p.setImageUrl(cellStr(row, 5));
                    players.add(p);
                }
                playerRepository.saveAll(players);
                playersCount = players.size();
            }

            Sheet matchesSheet = workbook.getSheet("Matches");
            if (matchesSheet != null) {
                List<IplMatch> matches = new ArrayList<>();
                for (Row row : matchesSheet) {
                    if (row.getRowNum() == 0) continue;
                    IplMatch m = new IplMatch();
                    m.setMatchNo(cellStr(row, 1));
                    m.setDateTime(cellStr(row, 2));
                    m.setHome(cellStr(row, 3));
                    m.setAway(cellStr(row, 4));
                    matches.add(m);
                }
                matchRepository.saveAll(matches);
                matchesCount = matches.size();
            }

            response.setStatus(true);
            response.setMessage("Imported: " + teamsCount + " teams, " + playersCount + " players, " + matchesCount + " matches");
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Import failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    private String cellStr(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }
}
