package com.nima.upquizz.service;

import com.nima.upquizz.entity.Category;
import com.nima.upquizz.entity.User;
import com.nima.upquizz.repository.CategoryRepository;
import com.nima.upquizz.repository.QuizRepository;
import com.nima.upquizz.request.CategoryRequest;
import com.nima.upquizz.response.CategoryResponse;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.util.FindUserAuthentication;
import com.nima.upquizz.util.PageResponseUtil;
import com.nima.upquizz.util.UserRoleIndicator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final FindUserAuthentication findUserAuthentication;
    private final UserRoleIndicator userRoleIndicator;
    private final QuizRepository quizRepository;
    private final PageResponseUtil pageResponseUtil;

    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            FindUserAuthentication findUserAuthentication,
            UserRoleIndicator userRoleIndicator,
            QuizRepository quizRepository, PageResponseUtil pageResponseUtil) {
        this.categoryRepository = categoryRepository;
        this.findUserAuthentication = findUserAuthentication;
        this.userRoleIndicator = userRoleIndicator;
        this.quizRepository = quizRepository;
        this.pageResponseUtil = pageResponseUtil;
    }

    @Transactional
    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        User user = findUserAuthentication.getAuthenticatedUser();
        boolean catExists = categoryRepository.findByName(categoryRequest.name()).isPresent();
        if (catExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists");
        }
        Category category = createCategoryFromRequest(categoryRequest);
        category.setId(0);
        Category savedCategory = categoryRepository.save(category);
        return createCategoryResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<CategoryResponse> getCategories(Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<Category> page = categoryRepository.findAll(pageable);
        List<CategoryResponse> responses = page.getContent().stream().map(this::createCategoryResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse getCategoryById(Long id) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return createCategoryResponse(category.get());
        }
        throw new  ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<CategoryResponse> searchCategories(String name, Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<Category> page = categoryRepository.findCategoriesByNameContains(name, pageable);
        List<CategoryResponse> responses = page.getContent().stream().map(this::createCategoryResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional
    @Override
    public void deleteCategoryById(Long id) {
        User user =  findUserAuthentication.getAuthenticatedUser();
        if (!userRoleIndicator.isUserAdmin(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can delete this category");
        }
        boolean categoryExists = categoryRepository.findById(id).isPresent();
        if (categoryExists) {
            categoryRepository.deleteById(id);
            return;
        }
        throw new  ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
    }

    @Transactional
    @Override
    public CategoryResponse updateCategory(long id, CategoryRequest categoryRequest) {
        User user = findUserAuthentication.getAuthenticatedUser();
        if (!userRoleIndicator.isUserAdmin(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can update this category");
        }
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        boolean nameTaken = categoryRepository.findByName(categoryRequest.name()).isPresent();
        if (nameTaken){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists");
        }
        category.get().setName(categoryRequest.name());
        return createCategoryResponse(categoryRepository.save(category.get()));
    }

    private Category createCategoryFromRequest(CategoryRequest request){
        return new Category(request.name());
    }

    private CategoryResponse createCategoryResponse(Category category){
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                quizRepository.countByCategory(category)
        );
    }
}
