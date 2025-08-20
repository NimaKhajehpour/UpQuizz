package com.nima.upquizz.repository;

import com.nima.upquizz.entity.Category;
import com.nima.upquizz.entity.Quiz;
import com.nima.upquizz.entity.Tag;
import com.nima.upquizz.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    int countByOwner(User owner);

    int countByCategory(Category category);

    int countByTags(List<Tag> tags);

    Optional<Quiz> findQuizByOwnerAndId(User owner, long id);

    Page<Quiz> findQuizzesByCategory_Id(long categoryId, Pageable pageable);

    Page<Quiz> findDistinctByTags_IdIn(List<Long> tags, Pageable pageable);

    Page<Quiz> getQuizzesByOwner(User owner, Pageable pageable);

    Page<Quiz> findDistinctByTitleContainsOrDescriptionContains(String title, String description, Pageable pageable);

    Optional<Quiz> getQuizByIdAndOwner(long id, User owner);

    void deleteQuizzesByTags_Id(long id);

    boolean existsByIdAndOwner(long id, User owner);
}
