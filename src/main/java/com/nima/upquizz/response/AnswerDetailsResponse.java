package com.nima.upquizz.response;

public record AnswerDetailsResponse(
        long id,
        String text,
        boolean correct
) {
}
