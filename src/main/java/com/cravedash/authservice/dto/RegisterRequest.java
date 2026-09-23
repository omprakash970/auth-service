package com.cravedash.authservice.dto;

import com.cravedash.authservice.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(

        @NotBlank
        String username,


        @NotBlank
        @Email
        String email,

        @NotBlank
        String password,

        @NotNull
        Role role,

        Long restaurantId

) {}