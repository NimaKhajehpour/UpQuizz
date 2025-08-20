package com.nima.upquizz.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnswerRequest(

        @NotEmpty(message = "answer text is mandatory")
        @Size(min = 3, message = "answer must be at least 3 characters long")
        String text,

        boolean correct
) {
}
