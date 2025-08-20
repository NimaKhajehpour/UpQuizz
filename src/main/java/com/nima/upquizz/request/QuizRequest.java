package com.nima.upquizz.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record QuizRequest(

        @NotEmpty(message = "title cant be empty")
        @Size(min = 10, message = "title must be at least 10 characters long")
        String title,

        @NotEmpty(message = "description text cant be empty")
        @Size(min = 10, message = "description must be at least 10 characters long")
        String description,

        @Valid
        @NotNull(message = "quiz mus have a category")
        CategoryRequest category,

        @Valid
        List<TagRequest> tags,

        @NotEmpty(message = "cant have a quiz without questions")
        @Valid
        List<QuestionRequest> questions
) {
}
