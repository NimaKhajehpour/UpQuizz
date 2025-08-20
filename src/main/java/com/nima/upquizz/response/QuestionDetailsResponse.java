package com.nima.upquizz.response;

import java.util.List;

public record QuestionDetailsResponse(
        long id,
        String text,
        List<AnswerDetailsResponse> answers
) {
}
