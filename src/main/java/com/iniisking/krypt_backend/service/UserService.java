package com.iniisking.krypt_backend.service;

import com.iniisking.krypt_backend.model.User;
import com.iniisking.krypt_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.iniisking.krypt_backend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordUtil passwordUtil;
    public User createUser(String email, String username, String password) {
        // Encrypt the password before saving
        String encryptedPassword = passwordUtil.encryptPassword(password);
        User user = new User(email, username, encryptedPassword); // Use encrypted password
        return userRepository.save(user);
    }
    public boolean validateUserPassword(String email, String plainPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordUtil.matchesPassword(plainPassword, user.getPassword());
        }
        return false;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUserLastLogin(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLastLogin(LocalDateTime.now());
            return userRepository.save(user);
        }
        return null;
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
}