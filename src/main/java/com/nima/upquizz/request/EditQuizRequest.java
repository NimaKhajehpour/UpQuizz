package com.nima.upquizz.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EditQuizRequest(

        @NotEmpty(message = "title cant be empty")
        @Size(min = 10, message = "title must be at least 10 characters long")
        String title,

        @NotEmpty(message = "description text cant be empty")
        @Size(min = 10, message = "description must be at least 10 characters long")
        String description
) {
}
