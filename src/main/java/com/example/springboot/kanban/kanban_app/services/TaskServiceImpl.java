package com.example.springboot.kanban.kanban_app.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springboot.kanban.kanban_app.models.Task;
import com.example.springboot.kanban.kanban_app.repositories.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository kanbanRepository;

    public TaskServiceImpl(TaskRepository kanbanRepository) {
        this.kanbanRepository = kanbanRepository;
    }

    @Override
    public Task createTask(Task task) {
        return kanbanRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return kanbanRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return kanbanRepository.findById(id).orElse(null);
    }

    @Override
    public Task updateTask(Long id, Task updatedTask) {
        return kanbanRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setStatus(updatedTask.getStatus());
            task.setPriority(updatedTask.getPriority());
            return kanbanRepository.save(task);
        }).orElse(null);
    }

    @Override
    public void deleteTask(Long id) {
        if (!kanbanRepository.existsById(id)) {
            throw new IllegalArgumentException("Tarea no encontrada con id: " + id);
        }
        kanbanRepository.deleteById(id);
    }

    @Override
    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return kanbanRepository.findByStatus(status);
    }

}