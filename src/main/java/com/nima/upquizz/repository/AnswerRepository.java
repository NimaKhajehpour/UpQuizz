package com.nima.upquizz.repository;

import com.nima.upquizz.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Page<Answer> findAnswersByQuestion_Id(long questionId, Pageable pageable);

    int countByQuestion_Id(long id);
}
