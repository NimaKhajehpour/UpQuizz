package com.nima.upquizz.repository;

import com.nima.upquizz.entity.Question;
import com.nima.upquizz.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findQuestionByOwnerAndId(User owner, long id);

    Page<Question> findQuestionsByQuiz_IdAndOwner(long quizId, User owner, Pageable pageable);

    int countByQuizId(long id);
}
