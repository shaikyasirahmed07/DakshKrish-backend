package com.sdk.kheeti.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sdk.kheeti.model.User;
import com.sdk.kheeti.repositories.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Register a new user and hash the password.
     *
     * @param user User object containing registration details.
     * @throws Exception if email already exists.
     */
    public void registerUser(User user) throws Exception {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already exists!");
        }

        // Encrypt the password using BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the database
        userRepository.save(user);
    }

    /**
     * Authenticate user using email and password.
     *
     * @param email    User's email.
     * @param password User's raw password.
     * @return The authenticated User if successful.
     * @throws Exception if login fails.
     */
    public User loginUser(String email, String password) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new Exception("Invalid email or password.");
        }

        User user = userOptional.get();

        // Validate the password using BCrypt
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid email or password.");
        }

        return user; // Return the authenticated user
    }

    /**
     * Check if an email exists in the database.
     *
     * @param email User's email.
     * @return True if the email exists, false otherwise.
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
