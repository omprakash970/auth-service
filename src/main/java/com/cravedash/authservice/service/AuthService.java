package com.cravedash.authservice.service;

import com.cravedash.authservice.dto.LoginRequest;
import com.cravedash.authservice.dto.RegisterRequest;
import com.cravedash.authservice.entity.Role;
import com.cravedash.authservice.entity.User;
import com.cravedash.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    public User getUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    public User register(RegisterRequest request) {

        if (userRepository.existsByUsername(
                request.username())) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(
                request.email())) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        // Owner must have a restaurant
        if (request.role() == Role.RESTAURANT_OWNER
                && request.restaurantId() == null) {

            throw new RuntimeException(
                    "Restaurant owner must have a restaurantId"
            );
        }

        Long restaurantId = request.role() == Role.RESTAURANT_OWNER
                ? request.restaurantId()
                : null;

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(
                        passwordEncoder.encode(
                                request.password()
                        )
                )
                .role(request.role())
                .restaurantId(restaurantId)
                .build();

        return userRepository.save(user);
    }

    public String login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username or password"
                        ));

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        return jwtService.generateToken(user);

    }
}