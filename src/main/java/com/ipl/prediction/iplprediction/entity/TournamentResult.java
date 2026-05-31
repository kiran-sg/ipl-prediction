package com.ipl.prediction.iplprediction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ipl_tournament_results")
public class TournamentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orange_cap_winner")
    private IplPlayer orangeCapWinner;

    @ManyToOne
    @JoinColumn(name = "purple_cap_winner")
    private IplPlayer purpleCapWinner;

    @ManyToOne
    @JoinColumn(name = "emerging_player_winner")
    private IplPlayer emergingPlayerWinner;

    @ManyToOne
    @JoinColumn(name = "fair_play_team_winner")
    private IplTeam fairPlayTeamWinner;

    @ManyToOne
    @JoinColumn(name = "most_fours_winner")
    private IplPlayer mostFoursWinner;

    @ManyToOne
    @JoinColumn(name = "most_sixes_winner")
    private IplPlayer mostSixesWinner;

    @ManyToOne
    @JoinColumn(name = "most_dot_balls_winner")
    private IplPlayer mostDotBallsWinner;

    @ManyToOne
    @JoinColumn(name = "best_bowling_fig_winner")
    private IplPlayer bestBowlingFigWinner;

    @ManyToOne
    @JoinColumn(name = "player_of_tournament_winner")
    private IplPlayer playerOfTournamentWinner;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
}
