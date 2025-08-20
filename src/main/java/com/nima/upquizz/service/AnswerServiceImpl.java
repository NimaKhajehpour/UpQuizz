package com.nima.upquizz.service;

import com.nima.upquizz.entity.Answer;
import com.nima.upquizz.entity.Question;
import com.nima.upquizz.entity.User;
import com.nima.upquizz.repository.AnswerRepository;
import com.nima.upquizz.repository.QuestionRepository;
import com.nima.upquizz.request.AnswerRequest;
import com.nima.upquizz.response.AnswerDetailsResponse;
import com.nima.upquizz.response.PageResponse;
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
public class AnswerServiceImpl implements AnswerService {

    private final FindUserAuthentication findUserAuthentication;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final PageResponseUtil pageResponseUtil;

    public AnswerServiceImpl(FindUserAuthentication findUserAuthentication, AnswerRepository answerRepository, QuestionRepository questionRepository, PageResponseUtil pageResponseUtil) {
        this.findUserAuthentication = findUserAuthentication;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.pageResponseUtil = pageResponseUtil;
    }

    @Transactional
    @Override
    public void createAnswer(long questionId, AnswerRequest answerRequest) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Question> question = questionRepository.findQuestionByOwnerAndId(user, questionId);
        if (question.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }
        Answer answer = createAnswer(answerRequest, question.get(), user);
        answer.setId(0);
        answerRepository.save(answer);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<AnswerDetailsResponse> getQuestionAnswers(long questionId, Pageable pageable) {
        User  user = findUserAuthentication.getAuthenticatedUser();
        Optional<Question> question = questionRepository.findQuestionByOwnerAndId(user, questionId);
        if (question.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }
        Page<Answer> page = answerRepository.findAnswersByQuestion_Id(questionId, pageable);
        List<AnswerDetailsResponse> responses = page.getContent().stream().map(this::createAnswerDetailsResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public AnswerDetailsResponse getAnswerById(long id) {
        User  user = findUserAuthentication.getAuthenticatedUser();
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found");
        }
        return createAnswerDetailsResponse(answer.get());
    }

    @Transactional
    @Override
    public AnswerDetailsResponse editAnswer(long id, AnswerRequest answerRequest) {
        User  user = findUserAuthentication.getAuthenticatedUser();
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found");
        }
        answer.get().setText(answerRequest.text());
        answer.get().setCorrect(answerRequest.correct());
        return createAnswerDetailsResponse(answerRepository.save(answer.get()));
    }

    @Transactional
    @Override
    public void deleteAnswer(long id) {
        User  user = findUserAuthentication.getAuthenticatedUser();
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found");
        }
        boolean canDeleteAnswer = answerRepository.countByQuestion_Id(answer.get().getQuestion().getId()) > 2;
        if (canDeleteAnswer){
            answerRepository.deleteById(id);
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cant delete last two answers of a question");
    }

    private Answer createAnswer(AnswerRequest answerRequest, Question question, User user) {
        return new Answer(
                answerRequest.text(),
                answerRequest.correct(),
                question,
                user
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
