package com.ipl.prediction.iplprediction.repository;

import com.ipl.prediction.iplprediction.entity.IplPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<IplPlayer, Long> {
    List<IplPlayer> findByTeamIn(List<String> teams);
    List<IplPlayer> findByPlayerNameAndTeam(String playerName, String team);
}
