package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.dto.IplUserDto;
import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.response.UserResponse;

import java.util.List;

public interface UserService {

    List<IplUser> getAllUsers();

    IplUser saveUser(IplUser iplUser);

    UserResponse validateUser(String userId);

    void uploadData(List<IplUserDto> employees);
}
