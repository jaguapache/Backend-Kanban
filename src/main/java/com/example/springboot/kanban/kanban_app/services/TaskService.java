package com.example.springboot.kanban.kanban_app.services;

import java.util.List;

import com.example.springboot.kanban.kanban_app.models.Task;
import com.example.springboot.kanban.kanban_app.repositories.KanbanRepository;

public class TaskService {
    KanbanRepository kanbanRepository = new KanbanRepository();

    public List<Task> getAllTasks() {
        return kanbanRepository.getAllTasks();
    }

    public Task getTaskById(Long id) {
        return kanbanRepository.getTaskById(id);
    }

}
