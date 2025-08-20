package com.nima.upquizz.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotEmpty(message = "username is mandatory")
        @Pattern(regexp = "^[^\\s]+$", message = "Username must not contain spaces")
        @Size(min = 5, message = "username must be at least 5 characters long")
        String username,

        @NotEmpty(message = "password is mandatory")
        @Size(min = 8, max = 20, message = "password must be between 8 and 20 characters long")
        @Pattern(regexp = "^[^\\s]+$", message = "Username must not contain spaces")
        String password
) {
}
