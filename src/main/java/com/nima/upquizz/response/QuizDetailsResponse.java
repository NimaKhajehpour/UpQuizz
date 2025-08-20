package com.nima.upquizz.response;

import java.util.List;

public record QuizDetailsResponse(
        long id,
        String title,
        String description,
        UserResponse owner,
        List<QuestionDetailsResponse> questions,
        List<TagResponse> tags,
        CategoryResponse category
) {
}
