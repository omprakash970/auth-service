package com.cravedash.authservice.controller;

import com.cravedash.authservice.dto.AuthResponse;
import com.cravedash.authservice.dto.LoginRequest;
import com.cravedash.authservice.dto.RegisterRequest;
import com.cravedash.authservice.dto.UserResponse;
import com.cravedash.authservice.entity.User;
import com.cravedash.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = authService.register(request);

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getRestaurantId()


        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        String token = authService.login(request);

        User user = authService
                .getUserByUsername(request.username());

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        "Bearer",
                        user.getUsername(),
                        user.getRole().name()
                )
        );
    }
}