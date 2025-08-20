package com.nima.upquizz.service;

import com.nima.upquizz.entity.*;
import com.nima.upquizz.repository.QuizRepository;
import com.nima.upquizz.repository.ReportRepository;
import com.nima.upquizz.request.ReportRequest;
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
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final FindUserAuthentication findUserAuthentication;
    private final QuizRepository quizRepository;
    private final PageResponseUtil pageResponseUtil;

    public ReportServiceImpl(ReportRepository reportRepository, FindUserAuthentication findUserAuthentication, QuizRepository quizRepository, PageResponseUtil pageResponseUtil) {
        this.reportRepository = reportRepository;
        this.findUserAuthentication = findUserAuthentication;
        this.quizRepository = quizRepository;
        this.pageResponseUtil = pageResponseUtil;
    }

    @Transactional
    @Override
    public void createReport(ReportRequest request, long quizId) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "quiz not found");
        }
        Report report = new Report(request.report(), user, quiz.get());
        report.setId(0);
        reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ReportResponse> getAllOwnReports(Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<Report> page = reportRepository.findAllByOwner(user, pageable);
        List<ReportResponse> responses = page.getContent().stream().map(this::createReportResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ReportResponse> getQuizReports(long quizId, Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        boolean quizExists = quizRepository.existsByIdAndOwner(quizId, user);
        if (quizExists) {
            Page<Report> page = reportRepository.findAllByQuiz_Id(quizId, pageable);
            List<ReportResponse> responses = page.getContent().stream().map(this::createReportResponse).toList();
            return pageResponseUtil.createPageResponse(responses, page);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "quiz not found");
    }

    @Transactional(readOnly = true)
    @Override
    public ReportResponse getReportById(long id) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Report> report = reportRepository.findByIdAndOwner(id, user);
        if (report.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "report not found");
        }
        return createReportResponse(report.get());
    }

    @Transactional
    @Override
    public void deleteReportById(long id) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Report> report = reportRepository.findByIdAndOwner(id, user);
        if (report.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "report not found");
        }
        reportRepository.delete(report.get());
    }

    private ReportResponse createReportResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getText(),
                report.getTimestamp(),
                createGeneralQuizResponse(report.getQuiz()),
                createUserResponse(report.getOwner())
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
}
