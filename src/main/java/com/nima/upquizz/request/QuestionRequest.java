package com.nima.upquizz.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record QuestionRequest(

        @NotEmpty(message = "question text cant be empty")
        @Size(min = 10, message = "question must be at least 10 characters long")
        String text,

        @NotEmpty(message = "cant have a question without answers")
        @Size(min = 2, message = "question must at least have two answers")
        @Valid
        List<AnswerRequest> answers
) {
}
