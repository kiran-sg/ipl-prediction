package com.ipl.prediction.iplprediction.controller;

import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.dto.IplUserDto;
import com.ipl.prediction.iplprediction.response.UserResponse;
import com.ipl.prediction.iplprediction.service.ExcelService;
import com.ipl.prediction.iplprediction.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ExcelService excelService;

    @GetMapping
    public List<IplUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public IplUser createUser(@RequestBody IplUser iplUser) {
        return userService.saveUser(iplUser);
    }

    @PostMapping(value = "/validate", consumes = "application/json")
    public ResponseEntity<UserResponse> validatePwd(@RequestBody IplUserDto user, HttpSession httpSession) {
        UserResponse response = new UserResponse();
        if (user.getUserId() == null) {
            response.setMessage("User id not provided");
            return ResponseEntity.badRequest().body(response); // Return false for bad request
        }

        response = userService.validateUser(user.getUserId());
        if(response.isValidUser()) {
            httpSession.setAttribute("userId", response.getUser().getUserId());
            System.out.println("UserId set in session: " + httpSession.getAttribute("userId"));
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        List<IplUserDto> employees = excelService.parseExcelFile(file);
        userService.uploadData(employees);
        return ResponseEntity.ok("File uploaded and data saved successfully!");
    }
}
