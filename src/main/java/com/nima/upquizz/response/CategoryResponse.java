package com.nima.upquizz.response;

public record CategoryResponse(
        long id,
        String name,
        long quizCount
) {
}
