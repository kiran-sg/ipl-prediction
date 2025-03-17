package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.entity.User;
import com.ipl.prediction.iplprediction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean validatePwd(String userId, String pwd) {
        User user = userRepository.findByUserId(userId);
        return user != null && user.getPwd().equals(pwd);
    }
}