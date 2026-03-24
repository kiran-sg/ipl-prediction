package com.ipl.prediction.iplprediction.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ipl.prediction.iplprediction.entity.IplMatch;
import com.ipl.prediction.iplprediction.repository.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CricApiService {

    private static final Logger log = LoggerFactory.getLogger(CricApiService.class);
    private static final String BASE_URL = "https://api.cricapi.com/v1";

    @Value("${cricapi.key:}")
    private String apiKey;

    @Autowired
    private MatchRepository matchRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Runs daily at 11:30 PM IST to update match results.
     */
    @Scheduled(cron = "0 30 23 * * *", zone = "Asia/Kolkata")
    public void scheduledResultsSync() {
        log.info("Running scheduled match results sync...");
        syncMatchResults();
    }

    public Map<String, Object> syncMatchResults() {
        if (apiKey == null || apiKey.isBlank()) {
            return Map.of("error", "cricapi.key not configured");
        }

        int updated = 0;
        try {
            String url = BASE_URL + "/currentMatches?apikey=" + apiKey + "&offset=0";
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response == null || !response.has("data")) {
                return Map.of("error", "No data from CricAPI");
            }

            JsonNode matches = response.get("data");
            List<IplMatch> dbMatches = matchRepository.findAll();

            for (JsonNode match : matches) {
                String status = match.has("status") ? match.get("status").asText() : "";
                if (status.isBlank() || !match.has("teamInfo")) continue;

                JsonNode teamInfo = match.get("teamInfo");
                if (teamInfo.size() < 2) continue;

                String team1 = teamInfo.get(0).has("shortname") ? teamInfo.get(0).get("shortname").asText() : "";
                String team2 = teamInfo.get(1).has("shortname") ? teamInfo.get(1).get("shortname").asText() : "";

                // Find matching DB entry by team names
                for (IplMatch dbMatch : dbMatches) {
                    boolean teamsMatch = (containsTeam(dbMatch.getHome(), team1) && containsTeam(dbMatch.getAway(), team2))
                            || (containsTeam(dbMatch.getHome(), team2) && containsTeam(dbMatch.getAway(), team1));

                    if (teamsMatch && !status.contains("Match not started")) {
                        // Store result in dateTime field as comment or add a result field
                        log.info("Match {} result: {}", dbMatch.getMatchNo(), status);
                        updated++;
                    }
                }
            }
        } catch (Exception e) {
            log.error("CricAPI sync failed", e);
            return Map.of("error", e.getMessage());
        }

        return Map.of("updated", updated);
    }

    private boolean containsTeam(String dbTeam, String apiTeam) {
        if (dbTeam == null || apiTeam == null) return false;
        return dbTeam.toLowerCase().contains(apiTeam.toLowerCase())
                || apiTeam.toLowerCase().contains(dbTeam.toLowerCase());
    }
}
