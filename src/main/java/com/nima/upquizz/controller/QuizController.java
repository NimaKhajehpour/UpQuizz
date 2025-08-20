package com.nima.upquizz.controller;

import com.nima.upquizz.entity.Quiz;
import com.nima.upquizz.request.EditQuizRequest;
import com.nima.upquizz.request.QuizRequest;
import com.nima.upquizz.request.TagRequest;
import com.nima.upquizz.response.GeneralQuizResponse;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.QuizDetailsResponse;
import com.nima.upquizz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "quiz endpoints", description = "endpoints used to access quizzes")
@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @Operation(summary = "create quiz")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createQuiz(@Valid @RequestBody QuizRequest quizRequest) {
        quizService.createQuiz(quizRequest);
    }

    @Operation(summary = "get all quizzes")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GeneralQuizResponse> getAllQuizzes(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return quizService.getAllQuizzes(pageRequest);
    }

    @Operation(summary = "get quizzes by category")
    @GetMapping("/category")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GeneralQuizResponse> getAllQuizzesByCategory(
            @RequestParam @Min(value = 1, message = "id cant be less than 1") long categoryId,
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return quizService.getAllQuizzesByCategory(pageRequest, categoryId);
    }

    @Operation(summary = "get quizzes by tag")
    @GetMapping("/tag")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GeneralQuizResponse> getAllQuizzesByTag(
            @RequestParam @Size(min = 1, message = "must at least add one tag id") List<Long> tagIds,
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return quizService.getAllQuizzesByTags(pageRequest, tagIds);
    }

    @Operation(summary = "get user quizzes")
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GeneralQuizResponse> getUserQuizzes(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return quizService.getOwnerQuizzes(pageRequest);
    }

    @Operation(summary = "get user quizzes by id")
    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GeneralQuizResponse> getUserQuizzesById(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size,
            @PathVariable @Min(value = 1, message = "id cant be less than 1") long id){
        PageRequest pageRequest = PageRequest.of(page, size);
        return quizService.getUserQuizzes(pageRequest, id);
    }

    @Operation(summary = "search quizzes")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GeneralQuizResponse> searchQuizzes(
            @RequestParam @Size(min = 2, message = "search query must be at least 2 characters long") String query,
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return quizService.searchQuizzes(pageRequest, query);
    }

    @Operation(summary = "get quiz by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GeneralQuizResponse getQuizById(@PathVariable @Min(value = 1, message = "id cant be less than 1") Long id) {
        return quizService.getQuizById(id);
    }

    @Operation(summary = "get quiz details by id")
    @GetMapping("/{id}/details")
    @ResponseStatus(HttpStatus.OK)
    public QuizDetailsResponse getQuizDetailById(@PathVariable @Min(value = 1, message = "id cant be less than 1") Long id) {
        return quizService.getQuizDetails(id);
    }

    @Operation(summary = "edit quiz")
    @PutMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GeneralQuizResponse editQuiz(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id, @Valid @RequestBody EditQuizRequest editQuizRequest) {
        return quizService.editQuiz(id, editQuizRequest);
    }

    @Operation(summary = "edit quiz tags")
    @PutMapping("/edit/{id}/tags")
    @ResponseStatus(HttpStatus.OK)
    public GeneralQuizResponse editTags(@RequestBody List<@Valid TagRequest> tagRequest, @PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        return quizService.editQuizTags(id, tagRequest);
    }

    @Operation(summary = "edit quiz category")
    @PutMapping("/edit/{id}/category")
    @ResponseStatus(HttpStatus.OK)
    public GeneralQuizResponse editCategory(@RequestBody @Min(value = 1,message = "category id cant be less than 1") long catId, @PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        return quizService.editQuizCategory(id, catId);
    }

    @Operation(summary = "delete quiz")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        quizService.deleteQuiz(id);
    }
}
