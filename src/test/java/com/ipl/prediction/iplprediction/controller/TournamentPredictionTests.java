package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.entity.IplPlayer;
import com.ipl.prediction.iplprediction.entity.IplTeam;
import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TournamentPredictionTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private PlayerRepository playerRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TournamentPredictionRepository tournamentPredictionRepository;

    private Long player1Id, player2Id, team1Id;

    @BeforeEach
    void setUp() {
        tournamentPredictionRepository.deleteAll();
        playerRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();

        IplUser user = new IplUser();
        user.setUserId("user2");
        user.setName("Test User");
        user.setPassword("");
        userRepository.save(user);

        IplPlayer p1 = new IplPlayer();
        p1.setPlayerNo("1");
        p1.setPlayerName("Virat Kohli");
        p1.setCategory("Batsman");
        p1.setTeam("RCB");
        p1 = playerRepository.save(p1);
        player1Id = p1.getId();

        IplPlayer p2 = new IplPlayer();
        p2.setPlayerNo("2");
        p2.setPlayerName("Jasprit Bumrah");
        p2.setCategory("Bowler");
        p2.setTeam("MI");
        p2 = playerRepository.save(p2);
        player2Id = p2.getId();

        IplTeam t1 = new IplTeam();
        t1.setShortName("CSK");
        t1.setTeamName("Chennai Super Kings");
        t1 = teamRepository.save(t1);
        team1Id = t1.getId();
    }

    @Test
    void saveTournamentPrediction_success() throws Exception {
        String body = """
            {
                "userId": "user2",
                "orangeCapPredictedId": %d,
                "purpleCapPredictedId": %d,
                "fairPlayTeamPredictedId": %d,
                "playerOfTournamentPredictedId": %d
            }
            """.formatted(player1Id, player2Id, team1Id, player1Id);

        mockMvc.perform(post("/api/predictions/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .cookie(new jakarta.servlet.http.Cookie("userId", "user2")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getTournamentPrediction_afterSave() throws Exception {
        // Save first
        String body = """
            {
                "userId": "user2",
                "orangeCapPredictedId": %d,
                "playerOfTournamentPredictedId": %d
            }
            """.formatted(player1Id, player1Id);

        mockMvc.perform(post("/api/predictions/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .cookie(new jakarta.servlet.http.Cookie("userId", "user2")))
                .andExpect(status().isOk());

        // Then get
        mockMvc.perform(get("/api/predictions/tournament")
                        .param("user", "user2")
                        .cookie(new jakarta.servlet.http.Cookie("userId", "user2")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.tournamentPrediction").exists())
                .andExpect(jsonPath("$.tournamentPrediction.orangeCapPredictedId").value(player1Id.intValue()))
                .andExpect(jsonPath("$.tournamentPrediction.playerOfTournamentPredictedId").value(player1Id.intValue()));
    }

    @Test
    void updateTournamentPrediction_overwritesPrevious() throws Exception {
        // Save initial
        String body1 = """
            {"userId": "user2", "orangeCapPredictedId": %d}
            """.formatted(player1Id);

        mockMvc.perform(post("/api/predictions/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body1)
                        .cookie(new jakarta.servlet.http.Cookie("userId", "user2")))
                .andExpect(status().isOk());

        // Update
        String body2 = """
            {"userId": "user2", "orangeCapPredictedId": %d}
            """.formatted(player2Id);

        mockMvc.perform(post("/api/predictions/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body2)
                        .cookie(new jakarta.servlet.http.Cookie("userId", "user2")))
                .andExpect(status().isOk());

        // Verify updated
        mockMvc.perform(get("/api/predictions/tournament")
                        .param("user", "user2")
                        .cookie(new jakarta.servlet.http.Cookie("userId", "user2")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tournamentPrediction.orangeCapPredictedId").value(player2Id.intValue()));
    }

    @Test
    void saveTournamentPrediction_withNullFields() throws Exception {
        String body = """
            {"userId": "user2", "orangeCapPredictedId": %d}
            """.formatted(player1Id);

        mockMvc.perform(post("/api/predictions/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .cookie(new jakarta.servlet.http.Cookie("userId", "user2")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));

        mockMvc.perform(get("/api/predictions/tournament")
                        .param("user", "user2")
                        .cookie(new jakarta.servlet.http.Cookie("userId", "user2")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tournamentPrediction.orangeCapPredictedId").value(player1Id.intValue()))
                .andExpect(jsonPath("$.tournamentPrediction.purpleCapPredictedId").isEmpty())
                .andExpect(jsonPath("$.tournamentPrediction.playerOfTournamentPredictedId").isEmpty());
    }
}
