package com.iniisking.krypt_backend.controller;
import com.iniisking.krypt_backend.model.User;
import com.iniisking.krypt_backend.service.UserService;
import com.iniisking.krypt_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password) {

        System.out.println("🔐 Login attempt for email: " + email);

        Map<String, Object> response = new HashMap<>();

        // Check if user exists
        Optional<User> userOptional = userService.getUserByEmail(email);
        System.out.println("👤 User found: " + userOptional.isPresent());

        if (userOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            System.out.println("❌ User not found");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate password
        System.out.println("🔑 Validating password...");
        boolean isValid = userService.validateUserPassword(email, password);
        System.out.println("✅ Password valid: " + isValid);

        if (!isValid) {
            response.put("success", false);
            response.put("message", "Invalid password");
            System.out.println("❌ Invalid password");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOptional.get();
        userService.updateUserLastLogin(user.getId());

        // Generate JWT token
        System.out.println("🔄 Generating JWT token...");
        try {
            String token = jwtUtil.generateToken(email, user.getId());
            System.out.println("✅ Token generated successfully");
            System.out.println("🔑 Token: " + token);

            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "username", user.getUsername()
            ));

            System.out.println("🎉 Login successful for user: " + email);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error generating token: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("message", "Authentication error");
            return ResponseEntity.status(500).body(response);
        }
    }
}