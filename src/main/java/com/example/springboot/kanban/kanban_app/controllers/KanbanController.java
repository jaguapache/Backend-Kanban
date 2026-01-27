package com.example.springboot.kanban.kanban_app.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.kanban.kanban_app.models.Task;
import com.example.springboot.kanban.kanban_app.services.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/kanban")
public class KanbanController {

    @GetMapping("/all-tasks")
    public List<Task> getAllTasks() {
        TaskService taskService = new TaskService();
        return taskService.getAllTasks();
    }

    @GetMapping("/task/{id}")
    public Task getTaskById(@PathVariable Long id) {
        TaskService taskService = new TaskService();
        return taskService.getTaskById(id);
    }

}
