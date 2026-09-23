package com.cravedash.authservice.dto;

public record UserResponse(

        Long id,
        String username,
        String email,
        String role,

        Long restaurantId

) {}