package com.example.springboot.kanban.kanban_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.kanban.kanban_app.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
