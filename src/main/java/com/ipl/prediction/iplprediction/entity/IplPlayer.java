package com.ipl.prediction.iplprediction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ipl_players")
public class IplPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_no")
    private String playerNo;

    @Column(name = "player_name")
    private String playerName;

    private String category;

    private String team;

    @Column(name = "image_url")
    private String imageUrl;
}
