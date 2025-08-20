package com.nima.upquizz.controller;

import com.nima.upquizz.request.ReportRequest;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.ReportResponse;
import com.nima.upquizz.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "reports endpoint", description = "endpoints used to access reports")
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "create report")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createReport(@Valid @RequestBody ReportRequest request, @RequestParam @Min(value = 1, message = "id cant be less than 1") long id){
        reportService.createReport(request,id);
    }

    @Operation(summary = "get owner reports")
    @GetMapping("/own")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ReportResponse> getOwnReports(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return reportService.getAllOwnReports(pageRequest);
    }

    @Operation(summary = "get quiz report")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ReportResponse> getQuizReports(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size,
            @RequestParam @Min(value = 1, message = "id cant be less than 1") long quizId
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return reportService.getQuizReports(quizId, pageRequest);
    }

    @Operation(summary = "get report by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReportResponse getReportById(
            @PathVariable @Min(value = 1, message = "id cant be less than 1") long id
    ){
        return reportService.getReportById(id);
    }

    @Operation(summary = "delete report")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReportById(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id){
        reportService.deleteReportById(id);
    }
}
