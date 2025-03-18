package com.ipl.prediction.iplprediction.service.impl;

import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.repository.UserRepository;
import com.ipl.prediction.iplprediction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public IplUser findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public IplUser saveUser(IplUser iplUser) {
        return userRepository.save(iplUser);
    }

    @Override
    public boolean validatePwd(String userId, String pwd) {
        IplUser iplUser = userRepository.findByUserId(userId);
        return iplUser != null && iplUser.getPwd().equals(pwd);
    }
}