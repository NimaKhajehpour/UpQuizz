package com.nima.upquizz.controller;

import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.ReportResponse;
import com.nima.upquizz.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin endpoints", description = "Endpoints that are used for admin")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Admin access for all reports")
    @GetMapping("/report/all")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ReportResponse> getAllReports(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return adminService.showAllReports(pageRequest);
    }

    @Operation(summary = "Admin access to quiz reports")
    @GetMapping("/report/quiz")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ReportResponse> getAllQuizReports(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size,
            @RequestParam @Min(value = 1, message = "id cant be less than 1") long id
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return adminService.showAllQuizReports(id, pageRequest);
    }

    @Operation(summary = "Admin access to user reports")
    @GetMapping("/report/user")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ReportResponse> getAllUserReports(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size,
            @RequestParam @Min(value = 1, message = "id cant be less than 1") long id
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return adminService.showAllUserReports(id, pageRequest);
    }

    @Operation(summary = "Admin access to a single report")
    @GetMapping("/report/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReportResponse showReport(
            @PathVariable @Min(value = 1, message = "id cant be less than 1") long id
    ) {
        return adminService.showReport(id);
    }
}
