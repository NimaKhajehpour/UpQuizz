package com.nima.upquizz.service;

import com.nima.upquizz.entity.User;
import com.nima.upquizz.request.ReportRequest;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.ReportResponse;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    void createReport(ReportRequest request, long quizId);
    PageResponse<ReportResponse> getAllOwnReports(Pageable pageable);
    PageResponse<ReportResponse> getQuizReports(long quizId, Pageable pageable);
    ReportResponse getReportById(long id);
    void deleteReportById(long id);
}
