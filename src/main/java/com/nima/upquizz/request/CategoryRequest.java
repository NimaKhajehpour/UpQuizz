package com.nima.upquizz.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotEmpty(message = "name is mandatory")
        @Size(min = 2, message = "category must be at least 2 characters long")
        String name
) {
}
