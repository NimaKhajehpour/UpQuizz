package com.nima.upquizz.service;

import com.nima.upquizz.entity.Answer;
import com.nima.upquizz.entity.Question;
import com.nima.upquizz.entity.Quiz;
import com.nima.upquizz.entity.User;
import com.nima.upquizz.repository.QuestionRepository;
import com.nima.upquizz.repository.QuizRepository;
import com.nima.upquizz.request.EditQuestionRequest;
import com.nima.upquizz.request.QuestionRequest;
import com.nima.upquizz.response.AnswerDetailsResponse;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.QuestionDetailsResponse;
import com.nima.upquizz.util.FindUserAuthentication;
import com.nima.upquizz.util.PageResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {


    private final FindUserAuthentication findUserAuthentication;
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;
    private final QuizRepository quizRepository;
    private final PageResponseUtil pageResponseUtil;

    public QuestionServiceImpl(FindUserAuthentication findUserAuthentication, QuestionRepository questionRepository, AnswerService answerService, QuizRepository quizRepository, PageResponseUtil pageResponseUtil) {
        this.findUserAuthentication = findUserAuthentication;
        this.questionRepository = questionRepository;
        this.answerService = answerService;
        this.quizRepository = quizRepository;
        this.pageResponseUtil = pageResponseUtil;
    }

    @Transactional
    @Override
    public void createQuestion(long quizId, QuestionRequest questionRequest) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Quiz> quiz = quizRepository.findQuizByOwnerAndId(user, quizId);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz Not Found");
        }
        Question question = createQuestionFromRequest(questionRequest, quiz.get(), user);
        question.setId(0);
        Question finalQuestion = questionRepository.save(question);
        questionRequest.answers().forEach(answer -> {
            answerService.createAnswer(finalQuestion.getId(), answer);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<QuestionDetailsResponse> getQuizQuestions(long quizId, Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Quiz> quiz = quizRepository.findQuizByOwnerAndId(user, quizId);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz Not Found");
        }
        Page<Question> page = questionRepository.findQuestionsByQuiz_IdAndOwner(quizId, user, pageable);
        List<QuestionDetailsResponse> responses = page.getContent().stream().map(this::createQuestionDetailsResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public QuestionDetailsResponse getQuestionById(long questionId) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Question> question = questionRepository.findQuestionByOwnerAndId(user, questionId);
        if (question.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question Not Found");
        }
        return createQuestionDetailsResponse(question.get());
    }

    @Transactional
    @Override
    public QuestionDetailsResponse editQuestion(long questionId, EditQuestionRequest editQuestionRequest) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Question> question = questionRepository.findQuestionByOwnerAndId(user, questionId);
        if (question.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question Not Found");
        }
        question.get().setText(editQuestionRequest.text());
        return createQuestionDetailsResponse(questionRepository.save(question.get()));
    }

    @Transactional
    @Override
    public void deleteQuestion(long questionId) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Question> question = questionRepository.findQuestionByOwnerAndId(user, questionId);
        if (question.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question Not Found");
        }
        boolean isLastQuestion =
                questionRepository.countByQuizId(question.get().getQuiz().getId()) < 2;
        if (isLastQuestion) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cant delete last question of the quiz, quiz may not be empty");
        }
        questionRepository.deleteById(questionId);
    }

    private Question createQuestionFromRequest(QuestionRequest questionRequest, Quiz quiz, User user) {
        return new Question(
                questionRequest.text(),
                quiz,
                user
        );
    }

    private QuestionDetailsResponse createQuestionDetailsResponse(Question question) {
        return new QuestionDetailsResponse(
                question.getId(),
                question.getText(),
                question.getAnswers().stream().map(this::createAnswerDetailsResponse).toList()
        );
    }

    private AnswerDetailsResponse createAnswerDetailsResponse(Answer answer) {
        return new AnswerDetailsResponse(
                answer.getId(),
                answer.getText(),
                answer.isCorrect()
        );
    }
}
