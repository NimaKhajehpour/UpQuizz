package com.nima.upquizz.repository;

import com.nima.upquizz.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    Page<Category> findCategoriesByNameContains(String name, Pageable pageable);

    Optional<Category> getCategoryByName(String name);
}
