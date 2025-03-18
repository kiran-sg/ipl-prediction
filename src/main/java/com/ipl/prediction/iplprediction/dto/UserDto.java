package com.ipl.prediction.iplprediction.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

    private Long id;
    private String userId;
    private String name;
    private String dept;
    private String location;
    private String pwd;
    private boolean active;

}
