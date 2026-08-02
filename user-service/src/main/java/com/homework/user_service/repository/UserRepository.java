package com.homework.user_service.repository;

import com.homework.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findByAge(Integer age);

    boolean existsByEmail(String email);
}
