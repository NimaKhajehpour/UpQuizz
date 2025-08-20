package com.nima.upquizz.repository;

import com.nima.upquizz.entity.Authority;
import com.nima.upquizz.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> getUserByUsername(String username);
    boolean existsByAuthorities(List<Authority> authorities);
    int countUserByAuthorities(List<Authority> authorities);
    Page<User> findUsersByUsernameContains(String username, Pageable pageable);
}
