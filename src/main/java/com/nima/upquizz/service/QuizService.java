package com.nima.upquizz.service;

import com.nima.upquizz.entity.Quiz;
import com.nima.upquizz.entity.Tag;
import com.nima.upquizz.entity.User;
import com.nima.upquizz.request.EditQuizRequest;
import com.nima.upquizz.request.QuizRequest;
import com.nima.upquizz.request.TagRequest;
import com.nima.upquizz.response.GeneralQuizResponse;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.QuizDetailsResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuizService {

    int getQuizCountByOwner(User owner);
    void createQuiz(QuizRequest quizRequest);
    PageResponse<GeneralQuizResponse> getAllQuizzes(Pageable pageable);
    GeneralQuizResponse getQuizById(Long id);
    PageResponse<GeneralQuizResponse> getAllQuizzesByCategory(Pageable pageable, Long id);
    PageResponse<GeneralQuizResponse> getAllQuizzesByTags(Pageable pageable, List<Long> ids);
    PageResponse<GeneralQuizResponse> getOwnerQuizzes(Pageable pageable);
    PageResponse<GeneralQuizResponse> getUserQuizzes(Pageable pageable, long ownerId);
    PageResponse<GeneralQuizResponse> searchQuizzes(Pageable pageable, String query);
    GeneralQuizResponse editQuizTags(long quizId, List<TagRequest> tags);
    GeneralQuizResponse editQuizCategory(long quizId, long CatId);
    GeneralQuizResponse editQuiz(long quizId, EditQuizRequest editQuizRequest);
    QuizDetailsResponse getQuizDetails(long id);
    void deleteQuiz(long id);
}
