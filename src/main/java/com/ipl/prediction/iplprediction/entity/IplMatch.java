package com.ipl.prediction.iplprediction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ipl_matches")
public class IplMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_no")
    private String matchNo;

    @Column(name = "date_time")
    private String dateTime;

    private String home;

    private String away;
}
