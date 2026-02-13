package com.example.springboot.kanban.kanban_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.kanban.kanban_app.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    List<User> findByEnabledTrue();
}
