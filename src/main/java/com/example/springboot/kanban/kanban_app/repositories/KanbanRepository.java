package com.example.springboot.kanban.kanban_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.kanban.kanban_app.models.Task;
import java.util.List;

public interface KanbanRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(Task.TaskStatus status);
}
