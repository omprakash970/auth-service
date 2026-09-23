package com.cravedash.authservice.dto;

public record AuthResponse(

        String token,
        String type,
        String username,
        String role

) {}