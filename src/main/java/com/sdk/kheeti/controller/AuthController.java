package com.sdk.kheeti.controller;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdk.kheeti.model.User;
import com.sdk.kheeti.service.UserService;

@RestController
@CrossOrigin(origins = "*") // Allow requests from React frontend
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @SuppressWarnings("empty-statement")
public ResponseEntity<?> login(@RequestBody User loginRequest) {
    User user = userService.findByUsername(loginRequest.getUsername());
    System.out.println("hello");
    if (user != null && userService.checkPassword(user, loginRequest.getPassword())) {
        // Instead of just a message, return a JSON response
        return ResponseEntity.ok(Map.of("success", true, "message", "Login successful!"));
    } else {
        return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid username or password."));
    }
}

}

