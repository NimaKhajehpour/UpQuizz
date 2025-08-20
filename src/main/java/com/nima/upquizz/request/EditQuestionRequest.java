package com.nima.upquizz.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EditQuestionRequest(

        @NotEmpty(message = "question text can not be empty")
        @Size(min = 10, message = "question text must be at least 10 characters long")
        String text
) {
}
