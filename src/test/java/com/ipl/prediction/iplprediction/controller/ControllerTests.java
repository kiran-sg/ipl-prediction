package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.entity.IplMatch;
import com.ipl.prediction.iplprediction.entity.IplPlayer;
import com.ipl.prediction.iplprediction.repository.MatchRepository;
import com.ipl.prediction.iplprediction.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @BeforeEach
    void setUp() {
        playerRepository.deleteAll();
        matchRepository.deleteAll();

        IplPlayer p1 = new IplPlayer();
        p1.setPlayerNo("1");
        p1.setPlayerName("Virat Kohli");
        p1.setCategory("Batsman");
        p1.setTeam("RCB");

        IplPlayer p2 = new IplPlayer();
        p2.setPlayerNo("2");
        p2.setPlayerName("Rohit Sharma");
        p2.setCategory("Batsman");
        p2.setTeam("MI");

        playerRepository.saveAll(List.of(p1, p2));

        IplMatch m1 = new IplMatch();
        m1.setMatchNo("1");
        m1.setDateTime("2025-03-22 19:30:00");
        m1.setHome("RCB");
        m1.setAway("MI");

        matchRepository.save(m1);
    }

    @Test
    void getAllMatches_returnsMatches() throws Exception {
        mockMvc.perform(get("/api/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].matchNo").value("1"))
                .andExpect(jsonPath("$[0].home").value("RCB"));
    }

    @Test
    void getPlayersByTeam_withFilter() throws Exception {
        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"teams\":[\"RCB\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].playerName").value("Virat Kohli"));
    }

    @Test
    void getPlayersByTeam_noFilter_returnsAll() throws Exception {
        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"teams\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
