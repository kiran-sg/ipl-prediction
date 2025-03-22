package com.ipl.prediction.iplprediction.response;

import com.ipl.prediction.iplprediction.dto.IplUserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String message;
    private boolean validUser;
    private IplUserDto user;
}
