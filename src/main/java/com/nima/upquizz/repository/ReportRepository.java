package com.nima.upquizz.repository;

import com.nima.upquizz.entity.Report;
import com.nima.upquizz.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    int countByOwner(User owner);
    Page<Report> findAllByOwner(User owner, Pageable pageable);

    Page<Report> findAllByQuiz_Id(long quizId, Pageable pageable);
    Optional<Report> findByIdAndOwner(long id, User owner);
}
