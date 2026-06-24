package com.frameskipt.frameskipt.security;

import com.frameskipt.frameskipt.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring Security calls this exactly once per login attempt.
    // Our job: find the user or throw UsernameNotFoundException.
    // We then convert our User entity into Spring's UserDetails
    // contract — the only shape Spring Security knows how to work with.
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        com.frameskipt.frameskipt.model.User user = userRepository
            .findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "No user found with username: " + username
                )
            );

        // Spring Security's own User object (not our entity).
        // Takes: username, the already-hashed password, and a role list.
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}