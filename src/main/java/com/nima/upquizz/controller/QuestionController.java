package com.nima.upquizz.controller;

import com.nima.upquizz.request.EditQuestionRequest;
import com.nima.upquizz.request.QuestionRequest;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.QuestionDetailsResponse;
import com.nima.upquizz.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "question endpoints", description = "endpoints used to access questions")
@RestController
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(summary = "create question")
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createQuestion(@Valid @RequestBody QuestionRequest questionRequest, @PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        questionService.createQuestion(id,  questionRequest);
    }

    @Operation(summary = "get quiz questions")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<QuestionDetailsResponse> getQuizQuestions(
            @RequestParam @Min(value = 1, message = "id cant be less than 1") long id,
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return questionService.getQuizQuestions(id, pageRequest);
    }

    @Operation(summary = "get question by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuestionDetailsResponse getQuestionById(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        return questionService.getQuestionById(id);
    }

    @Operation(summary = "edit question")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuestionDetailsResponse editQuestion(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id, @Valid @RequestBody EditQuestionRequest editQuestionRequest) {
        return questionService.editQuestion(id, editQuestionRequest);
    }

    @Operation(summary = "delete question")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        questionService.deleteQuestion(id);
    }
}
