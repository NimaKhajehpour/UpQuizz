package com.nima.upquizz.response;

import java.util.List;

public record GeneralQuizResponse(
        long id,
        String title,
        String description,
        UserResponse owner,
        List<TagResponse> tags,
        CategoryResponse category
) {
}
