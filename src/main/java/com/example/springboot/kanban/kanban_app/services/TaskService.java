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

}
