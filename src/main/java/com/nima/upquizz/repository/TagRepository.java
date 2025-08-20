package com.nima.upquizz.repository;

import com.nima.upquizz.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findTagByName(String name);

    Page<Tag> findTagsByNameContains(String name, Pageable pageable);

    List<Tag> findAllByNameIn(Collection<String> names);
}
