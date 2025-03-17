package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.entity.User;
import com.ipl.prediction.iplprediction.request.UserDto;
import com.ipl.prediction.iplprediction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping(value = "/validate", consumes = "application/json")
    public ResponseEntity<Boolean> validatePwd(@RequestBody UserDto user) {
        // Validate input
        if (user.getUserId() == null || user.getPwd() == null) {
            return ResponseEntity.badRequest().body(false); // Return false for bad request
        }

        // Validate password using the service
        boolean isValid = userService.validatePwd(user.getUserId(), user.getPwd());

        // Return the result as ResponseEntity<Boolean>
        return ResponseEntity.ok(isValid);
    }
}
