package com.example.springboot.kanban.kanban_app.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springboot.kanban.kanban_app.models.Task;
import com.example.springboot.kanban.kanban_app.repositories.KanbanRepository;

@Service
public class TaskService {

    private final KanbanRepository kanbanRepository;

    public TaskService(KanbanRepository kanbanRepository) {
        this.kanbanRepository = kanbanRepository;
    }

    public Task createTask(Task task) {
        return kanbanRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return kanbanRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return kanbanRepository.findById(id).orElse(null);
    }

    public Task updateTask(Long id, Task updatedTask) {
        return kanbanRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setStatus(updatedTask.getStatus());
            task.setPriority(updatedTask.getPriority());
            return kanbanRepository.save(task);
        }).orElse(null);
    }

    public void deleteTask(Long id) {
        if (!kanbanRepository.existsById(id)) {
            throw new IllegalArgumentException("Tarea no encontrada con id: " + id);
        }
        kanbanRepository.deleteById(id);
    }

    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return kanbanRepository.findByStatus(status);
    }

}
