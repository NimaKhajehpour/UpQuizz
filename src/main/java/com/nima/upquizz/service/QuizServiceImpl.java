package com.nima.upquizz.service;

import com.nima.upquizz.entity.*;
import com.nima.upquizz.repository.*;
import com.nima.upquizz.request.AnswerRequest;
import com.nima.upquizz.request.EditQuizRequest;
import com.nima.upquizz.request.QuizRequest;
import com.nima.upquizz.request.TagRequest;
import com.nima.upquizz.response.*;
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
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final FindUserAuthentication findUserAuthentication;
    private final CategoryRepository categoryRepository;
    private final QuestionService questionService;
    private final TagRepository tagRepository;
    private final ReportRepository reportRepository;
    private final PageResponseUtil pageResponseUtil;
    private final UserRepository userRepository;

    public QuizServiceImpl(QuizRepository quizRepository, FindUserAuthentication findUserAuthentication, CategoryRepository categoryRepository, QuestionService questionService, TagRepository tagRepository, ReportRepository reportRepository, PageResponseUtil pageResponseUtil, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.findUserAuthentication = findUserAuthentication;
        this.categoryRepository = categoryRepository;
        this.questionService = questionService;
        this.tagRepository = tagRepository;
        this.reportRepository = reportRepository;
        this.pageResponseUtil = pageResponseUtil;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public int getQuizCountByOwner(User owner) {
        return quizRepository.countByOwner(owner);
    }

    @Transactional
    @Override
    public void createQuiz(QuizRequest quizRequest) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Category> category = categoryRepository.getCategoryByName(quizRequest.category().name());
        if (category.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        List<Tag> tags = tagRepository.findAllByNameIn(quizRequest.tags().stream().map(TagRequest::name).toList());
        Quiz quiz = createQuizFromRequest(quizRequest, user, category.get(), tags);
        quiz.setId(0);
        Quiz finalQuiz = quizRepository.save(quiz);
        quizRequest.questions().forEach(question -> {
            questionService.createQuestion(finalQuiz.getId(), question);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<GeneralQuizResponse> getAllQuizzes(Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<Quiz> page = quizRepository.findAll(pageable);
        List<GeneralQuizResponse> responses = page.getContent().stream().map(this::createGeneralQuizResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public GeneralQuizResponse getQuizById(Long id) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }
        return createGeneralQuizResponse(quiz.get());
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<GeneralQuizResponse> getAllQuizzesByCategory(Pageable pageable, Long id) {
        User user = findUserAuthentication.getAuthenticatedUser();
        boolean categoryExists = categoryRepository.existsById(id);
        if (categoryExists) {
            Page<Quiz> page = quizRepository.findQuizzesByCategory_Id(id, pageable);
            List<GeneralQuizResponse> responses = page.getContent().stream().map(this::createGeneralQuizResponse).toList();
            return pageResponseUtil.createPageResponse(responses, page);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<GeneralQuizResponse> getAllQuizzesByTags(Pageable pageable, List<Long> ids) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<Quiz> page = quizRepository.findDistinctByTags_IdIn(ids, pageable);
        List<GeneralQuizResponse> responses = page.getContent().stream().map(this::createGeneralQuizResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<GeneralQuizResponse> getOwnerQuizzes(Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<Quiz> page = quizRepository.getQuizzesByOwner(user, pageable);
        List<GeneralQuizResponse> responses = page.getContent().stream().map(this::createGeneralQuizResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<GeneralQuizResponse> getUserQuizzes(Pageable pageable, long ownerId) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
        }
        Page<Quiz> page = quizRepository.getQuizzesByOwner(owner.get(), pageable);
        List<GeneralQuizResponse> responses = page.getContent().stream().map(this::createGeneralQuizResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<GeneralQuizResponse> searchQuizzes(Pageable pageable, String query) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<Quiz> page = quizRepository.findDistinctByTitleContainsOrDescriptionContains(query, query, pageable);
        List<GeneralQuizResponse> responses = page.getContent().stream().map(this::createGeneralQuizResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional
    @Override
    public GeneralQuizResponse editQuizTags(long quizId, List<TagRequest> tags) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Quiz> quiz = quizRepository.getQuizByIdAndOwner(quizId, user);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }
        List<Tag> realTags = tagRepository.findAllByNameIn(tags.stream().map(TagRequest::name).toList());
        quiz.get().setTags(realTags);
        return createGeneralQuizResponse(quizRepository.save(quiz.get()));
    }

    @Transactional
    @Override
    public GeneralQuizResponse editQuizCategory(long quizId, long CatId) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Quiz> quiz = quizRepository.getQuizByIdAndOwner(quizId, user);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }
        Optional<Category> cat = categoryRepository.findById(CatId);
        if (cat.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        quiz.get().setCategory(cat.get());
        return createGeneralQuizResponse(quizRepository.save(quiz.get()));
    }

    @Transactional
    @Override
    public GeneralQuizResponse editQuiz(long quizId, EditQuizRequest editQuizRequest) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Quiz> quiz = quizRepository.getQuizByIdAndOwner(quizId, user);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }
        quiz.get().setTitle(editQuizRequest.title());
        quiz.get().setDescription(editQuizRequest.description());
        return createGeneralQuizResponse(quizRepository.save(quiz.get()));
    }

    @Transactional(readOnly = true)
    @Override
    public QuizDetailsResponse getQuizDetails(long id) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }
        Quiz finalQuiz = quiz.get();
        return new QuizDetailsResponse(
                finalQuiz.getId(),
                finalQuiz.getTitle(),
                finalQuiz.getDescription(),
                createUserResponse(finalQuiz.getOwner()),
                finalQuiz.getQuestions().stream().map(this::createQuestionDetailsResponse).toList(),
                finalQuiz.getTags().stream().map(this::createTagResponse).toList(),
                createCategoryResponse(finalQuiz.getCategory())
        );
    }

    @Transactional
    @Override
    public void deleteQuiz(long id) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Quiz> quiz = quizRepository.getQuizByIdAndOwner(id, user);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }
        quizRepository.deleteById(id);
    }

    private Quiz createQuizFromRequest(QuizRequest quizRequest, User user, Category category, List<Tag> tags) {
        return new Quiz(
                quizRequest.title(),
                quizRequest.description(),
                user,
                category,
                tags
        );
    }

    private GeneralQuizResponse createGeneralQuizResponse(Quiz quiz) {
        return new GeneralQuizResponse(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                createUserResponse(quiz.getOwner()),
                quiz.getTags().stream().map(this::createTagResponse).toList(),
                createCategoryResponse(quiz.getCategory())
        );
    }

    private UserResponse createUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getAuthorities().stream().map(it -> new Authority(it.getAuthority())).toList(),
                quizRepository.countByOwner(user),
                reportRepository.countByOwner(user)
        );
    }

    private TagResponse createTagResponse(Tag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getName(),
                quizRepository.countByTags(List.of(tag))
        );
    }

    private CategoryResponse createCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                quizRepository.countByCategory(category)
        );
    }

    private Tag createTagFromRequest(TagRequest tagRequest) {
        return new Tag(
                tagRequest.name()
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
