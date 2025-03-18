package com.ipl.prediction.iplprediction.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LeaderboardDTO {
    private String userId;
    private String location;
    private Integer totalPoints;
    private Integer position;

    public LeaderboardDTO(String userId, String location,
                          Integer totalPoints, Integer position) {
        this.userId = userId;
        this.location = location;
        this.totalPoints = totalPoints;
        this.position = position;
    }

}
