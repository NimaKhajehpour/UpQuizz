package com.nima.upquizz.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest(
        @NotEmpty(message = "password is mandatory")
        @Size(min = 8, max = 20, message = "password must be between 8 and 20 characters long")
        @Pattern(regexp = "^[^\\s]+$", message = "Username must not contain spaces")
        String oldPassword,

        @NotEmpty(message = "password is mandatory")
        @Size(min = 8, max = 20, message = "password must be between 8 and 20 characters long")
        @Pattern(regexp = "^[^\\s]+$", message = "Username must not contain spaces")
        String newPassword,

        @NotEmpty(message = "password is mandatory")
        @Size(min = 8, max = 20, message = "password must be between 8 and 20 characters long")
        @Pattern(regexp = "^[^\\s]+$", message = "Username must not contain spaces")
        String confirmPassword
) {
}
