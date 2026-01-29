package com.example.springboot.kanban.kanban_app.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.kanban.kanban_app.models.Task;
import com.example.springboot.kanban.kanban_app.services.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/kanban")
public class KanbanController {

    private final TaskService taskService;

    public KanbanController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("task")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.status(tasks.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(tasks);
    }

    @PutMapping("task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        if (updatedTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea no encontrada con id: " + id);
        }
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarea no encontrada con id: " + id);
        }
    }

    @GetMapping("/getByStatus/{status}")
    public String getByStatus(@PathVariable String status) {
        return taskService.getTasksByStatus(Task.TaskStatus.valueOf(status)).toString();

    }

}
