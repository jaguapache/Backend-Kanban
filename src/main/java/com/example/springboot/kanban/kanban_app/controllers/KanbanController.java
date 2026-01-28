package com.example.springboot.kanban.kanban_app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.kanban.kanban_app.models.Task;
import com.example.springboot.kanban.kanban_app.services.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/kanban")
public class KanbanController {

    private final TaskService taskService;

    public KanbanController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("task")
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping("tasks")
    public String getAllTasks() {
        return taskService.getAllTasks().toString();
    }

}
