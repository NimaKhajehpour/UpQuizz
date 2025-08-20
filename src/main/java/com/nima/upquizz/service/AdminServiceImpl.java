package com.nima.upquizz.service;

import com.nima.upquizz.entity.*;
import com.nima.upquizz.repository.QuizRepository;
import com.nima.upquizz.repository.ReportRepository;
import com.nima.upquizz.repository.UserRepository;
import com.nima.upquizz.response.*;
import com.nima.upquizz.util.PageResponseUtil;
import jdk.jfr.TransitionTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
public class AdminServiceImpl implements AdminService {


    private final ReportRepository reportRepository;
    private final QuizRepository quizRepository;
    private final PageResponseUtil pageResponseUtil;
    private final UserRepository userRepository;

    public AdminServiceImpl(ReportRepository reportRepository, QuizRepository quizRepository, PageResponseUtil pageResponseUtil, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.quizRepository = quizRepository;
        this.pageResponseUtil = pageResponseUtil;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ReportResponse> showAllReports(Pageable pageable) {
        Page<Report> page = reportRepository.findAll(pageable);
        List<ReportResponse> responses = page.getContent().stream().map(this::createReportResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ReportResponse> showAllQuizReports(long quizId, Pageable pageable) {
        Page<Report> page = reportRepository.findAllByQuiz_Id(quizId, pageable);
        List<ReportResponse> responses = page.getContent().stream().map(this::createReportResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ReportResponse> showAllUserReports(long userId, Pageable pageable) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        Page<Report> page = reportRepository.findAllByOwner(user.get(), pageable);
        List<ReportResponse> responses = page.getContent().stream().map(this::createReportResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public ReportResponse showReport(long reportId) {
        Optional<Report> report = reportRepository.findById(reportId);
        if (report.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found");
        }
        return createReportResponse(report.get());
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

    private UserResponse createUserResponse(User owner) {
        return new UserResponse(
                owner.getId(),
                owner.getUsername(),
                owner.getAuthorities().stream().map(it -> new Authority(it.getAuthority())).toList(),
                quizRepository.countByOwner(owner),
                reportRepository.countByOwner(owner)
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

    private CategoryResponse createCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                quizRepository.countByCategory(category)
        );
    }

    private TagResponse createTagResponse(Tag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getName(),
                quizRepository.countByTags(List.of(tag))
        );
    }
}
