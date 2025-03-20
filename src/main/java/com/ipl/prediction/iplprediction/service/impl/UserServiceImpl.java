package com.ipl.prediction.iplprediction.service.impl;

import com.ipl.prediction.iplprediction.dto.IplUserDto;
import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.repository.UserRepository;
import com.ipl.prediction.iplprediction.response.UserResponse;
import com.ipl.prediction.iplprediction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<IplUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public IplUser saveUser(IplUser iplUser) {
        return userRepository.save(iplUser);
    }

    @Override
    public UserResponse validateUser(String userId) {
        UserResponse response = new UserResponse();
        IplUser iplUser = userRepository.findByUserId(userId);
        boolean validUser = iplUser != null && iplUser.getUserId().equals(userId);
        response.setValidUser(validUser);
        if (validUser) {
            response.setUser(iplUserToUserDto(iplUser));
        }
        return response;
    }

    @Override
    public void uploadData(List<IplUserDto> dtos) {
        List<IplUser> users = new ArrayList<>();
        dtos.forEach(dto -> {
            IplUser user = new IplUser();
            user.setUserId(dto.getUserId());
            user.setName(dto.getName());
            user.setLocation(dto.getLocation());
            users.add(user);
        });
        userRepository.saveAll(users);
    }

    private IplUserDto iplUserToUserDto(IplUser user) {
        IplUserDto iplUserDto = new IplUserDto();
        iplUserDto.setUserId(user.getUserId());
        iplUserDto.setName(user.getName());
        iplUserDto.setLocation(user.getLocation());
        return iplUserDto;
    }
}