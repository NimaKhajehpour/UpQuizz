package com.nima.upquizz.controller;

import com.nima.upquizz.request.AnswerRequest;
import com.nima.upquizz.response.AnswerDetailsResponse;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Answer endpoints", description = "endpoints that are used to access answers")
@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @Operation(summary = "create answer")
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAnswer(@Valid @RequestBody AnswerRequest answerRequest, @PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        answerService.createAnswer(id,  answerRequest);
    }

    @Operation(summary = "get question answers")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<AnswerDetailsResponse> getQuestionAnswers(
            @RequestParam @Min(value = 1, message = "id cant be less than 1") long id,
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return answerService.getQuestionAnswers(id, pageRequest);
    }

    @Operation(summary = "get answer by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnswerDetailsResponse getAnswerById(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        return answerService.getAnswerById(id);
    }

    @Operation(summary = "edit answer")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnswerDetailsResponse editAnswer(@Valid @RequestBody AnswerRequest answerRequest, @PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        return answerService.editAnswer(id, answerRequest);
    }

    @Operation(summary = "delete answer")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnswer(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        answerService.deleteAnswer(id);
    }

}
