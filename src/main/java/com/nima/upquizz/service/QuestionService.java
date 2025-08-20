package com.nima.upquizz.service;

import com.nima.upquizz.entity.Question;
import com.nima.upquizz.entity.User;
import com.nima.upquizz.request.EditQuestionRequest;
import com.nima.upquizz.request.QuestionRequest;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.QuestionDetailsResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuestionService {

    void createQuestion(long quizId, QuestionRequest questionRequest);
    PageResponse<QuestionDetailsResponse> getQuizQuestions(long quizId, Pageable pageable);
    QuestionDetailsResponse getQuestionById(long questionId);
    QuestionDetailsResponse editQuestion(long questionId, EditQuestionRequest editQuestionRequest);
    void deleteQuestion(long questionId);
}
