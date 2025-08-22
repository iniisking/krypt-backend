package com.iniisking.krypt_backend.controller;

import com.iniisking.krypt_backend.model.User;
import com.iniisking.krypt_backend.repository.UserRepository;
import com.iniisking.krypt_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // ADD THIS ANNOTATION
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/health")
    public String health() {
        return "Debug endpoint working!";
    }

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/jwt-test")
    public String jwtTest() {
        try {
            System.out.println("Testing JWT generation...");
            String token = jwtUtil.generateToken("test@example.com", 1L);
            System.out.println("Token generated: " + token);

            boolean isValid = jwtUtil.validateToken(token);
            System.out.println("Token valid: " + isValid);

            return "Token: " + token + " | Valid: " + isValid;
        } catch (Exception e) {
            System.out.println("JWT Test Error: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}