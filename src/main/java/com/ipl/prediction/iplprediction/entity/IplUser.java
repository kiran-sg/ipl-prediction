package com.ipl.prediction.iplprediction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ipl_users")
public class IplUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private String userId;
    private String name;
    private String location;
    @Column(name = "prev_points")
    private Integer prevPoints;

}
