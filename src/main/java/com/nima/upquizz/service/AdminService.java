package com.nima.upquizz.service;

import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.ReportResponse;
import org.springframework.data.domain.Pageable;


public interface AdminService {

    PageResponse<ReportResponse> showAllReports(Pageable pageable);
    PageResponse<ReportResponse> showAllQuizReports(long quizId, Pageable pageable);
    PageResponse<ReportResponse> showAllUserReports(long userId, Pageable pageable);
    ReportResponse showReport(long reportId);
}
