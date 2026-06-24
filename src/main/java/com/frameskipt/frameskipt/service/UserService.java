package com.frameskipt.frameskipt.service;

import com.frameskipt.frameskipt.model.User;
import com.frameskipt.frameskipt.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring injects the BCryptPasswordEncoder bean declared
    // in SecurityConfig — constructor injection wires it automatically.
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(
                "Username '" + user.getUsername() + "' is already taken."
            );
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                "An account with email '" + user.getEmail() + "' already exists."
            );
        }

        // TODO resolved: BCrypt replaces plaintext before persisting.
        // encode() applies a random salt each time, so the same password
        // produces a different hash on every call — by design.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No user found with id: " + id
            ));
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException(
                "No user found with username: " + username
            ));
    }
}