package com.nima.upquizz.controller;

import com.nima.upquizz.request.CategoryRequest;
import com.nima.upquizz.response.CategoryResponse;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "category endpoints", description = "endpoints used to access categories")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "create category")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request){
        return categoryService.createCategory(request);
    }

    @Operation(summary = "get categories")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<CategoryResponse> getCategories(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.getCategories(pageable);
    }

    @Operation(summary = "search categories")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<CategoryResponse> searchCategories(
            @RequestParam @Size(min = 2, message = "category name must at least have two characters") String name,
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.searchCategories(name, pageable);
    }

    @Operation(summary = "get category by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getCategoryById(@PathVariable @Min(value = 1, message = "id cant be less than 1") Long id){
        return categoryService.getCategoryById(id);
    }

    @Operation(summary = "delete category")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable @Min(value = 1, message = "id cant be less than 1") Long id){
        categoryService.deleteCategoryById(id);
    }

    @Operation(summary = "update category")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse updateCategory(@PathVariable @Min(value = 1, message = "id cant be less than 1") Long id, @Valid @RequestBody CategoryRequest request){
        return categoryService.updateCategory(id, request);
    }
}
