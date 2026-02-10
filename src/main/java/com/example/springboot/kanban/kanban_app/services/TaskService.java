package com.example.springboot.kanban.kanban_app.services;

import java.util.List;

import com.example.springboot.kanban.kanban_app.models.Task;

public interface TaskService {

    Task createTask(Task task);

    List<Task> getAllTasks();

    Task getTaskById(Long id);

    Task updateTask(Long id, Task updatedTask);

    void deleteTask(Long id);

    List<Task> getTasksByStatus(Task.TaskStatus status);

}
