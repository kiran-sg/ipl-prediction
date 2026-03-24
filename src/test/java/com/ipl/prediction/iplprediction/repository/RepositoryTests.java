package com.ipl.prediction.iplprediction.repository;

import com.ipl.prediction.iplprediction.entity.IplMatch;
import com.ipl.prediction.iplprediction.entity.IplPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RepositoryTests {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @BeforeEach
    void setUp() {
        playerRepository.deleteAll();
        matchRepository.deleteAll();

        IplPlayer player1 = new IplPlayer();
        player1.setPlayerNo("1");
        player1.setPlayerName("Virat Kohli");
        player1.setCategory("Batsman");
        player1.setTeam("RCB");

        IplPlayer player2 = new IplPlayer();
        player2.setPlayerNo("2");
        player2.setPlayerName("Rohit Sharma");
        player2.setCategory("Batsman");
        player2.setTeam("MI");

        IplPlayer player3 = new IplPlayer();
        player3.setPlayerNo("3");
        player3.setPlayerName("Jasprit Bumrah");
        player3.setCategory("Bowler");
        player3.setTeam("MI");

        playerRepository.saveAll(List.of(player1, player2, player3));

        IplMatch match1 = new IplMatch();
        match1.setMatchNo("1");
        match1.setDateTime("2025-03-22 19:30:00");
        match1.setHome("RCB");
        match1.setAway("MI");

        IplMatch match2 = new IplMatch();
        match2.setMatchNo("2");
        match2.setDateTime("2025-03-23 15:30:00");
        match2.setHome("CSK");
        match2.setAway("DC");

        matchRepository.saveAll(List.of(match1, match2));
    }

    @Test
    void findAllPlayers_returnsAll() {
        List<IplPlayer> players = playerRepository.findAll();
        assertThat(players).hasSize(3);
    }

    @Test
    void findPlayersByTeam_filtersCorrectly() {
        List<IplPlayer> miPlayers = playerRepository.findByTeamIn(List.of("MI"));
        assertThat(miPlayers).hasSize(2);
        assertThat(miPlayers).allMatch(p -> p.getTeam().equals("MI"));
    }

    @Test
    void findPlayersByMultipleTeams() {
        List<IplPlayer> players = playerRepository.findByTeamIn(List.of("MI", "RCB"));
        assertThat(players).hasSize(3);
    }

    @Test
    void findPlayersByTeam_noMatch_returnsEmpty() {
        List<IplPlayer> players = playerRepository.findByTeamIn(List.of("SRH"));
        assertThat(players).isEmpty();
    }

    @Test
    void findAllMatches_returnsAll() {
        List<IplMatch> matches = matchRepository.findAll();
        assertThat(matches).hasSize(2);
    }

    @Test
    void findMatchByMatchNo_found() {
        Optional<IplMatch> match = matchRepository.findByMatchNo("1");
        assertThat(match).isPresent();
        assertThat(match.get().getHome()).isEqualTo("RCB");
        assertThat(match.get().getAway()).isEqualTo("MI");
    }

    @Test
    void findMatchByMatchNo_notFound() {
        Optional<IplMatch> match = matchRepository.findByMatchNo("99");
        assertThat(match).isEmpty();
    }
}
