package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.response.UserResponse;

import java.util.List;

public interface UserService {

    List<IplUser> getAllUsers();

    IplUser findByUserId(String userId);

    IplUser saveUser(IplUser iplUser);

    UserResponse validateUser(String userId);
}
