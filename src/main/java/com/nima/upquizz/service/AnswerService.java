package com.nima.upquizz.service;

import com.nima.upquizz.entity.Answer;
import com.nima.upquizz.request.AnswerRequest;
import com.nima.upquizz.response.AnswerDetailsResponse;
import com.nima.upquizz.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface AnswerService {

    void createAnswer(long questionId, AnswerRequest answerRequest);
    PageResponse<AnswerDetailsResponse> getQuestionAnswers(long questionId, Pageable pageable);
    AnswerDetailsResponse getAnswerById(long id);
    AnswerDetailsResponse editAnswer(long id, AnswerRequest answerRequest);
    void deleteAnswer(long id);
}
