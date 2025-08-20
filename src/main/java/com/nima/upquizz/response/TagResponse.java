package com.nima.upquizz.response;

public record TagResponse(
        long id,
        String name,
        int quizCount
) {
}
