package com.nima.upquizz.service;

import com.nima.upquizz.entity.Category;
import com.nima.upquizz.request.CategoryRequest;
import com.nima.upquizz.response.CategoryResponse;
import com.nima.upquizz.response.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    PageResponse<CategoryResponse> getCategories(Pageable pageable);
    CategoryResponse getCategoryById(Long id);
    PageResponse<CategoryResponse> searchCategories(String name, Pageable pageable);
    void deleteCategoryById(Long id);
    CategoryResponse updateCategory(long id, CategoryRequest categoryRequest);
}
