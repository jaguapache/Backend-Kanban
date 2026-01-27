package com.example.springboot.kanban.kanban_app.repositories;

import java.util.Arrays;
import java.util.List;

import com.example.springboot.kanban.kanban_app.models.Task;

public class KanbanRepository {

    private List<Task> tasks;

    public KanbanRepository() {

        this.tasks = Arrays.asList(
                new Task(1L, "Prueba 1", "TODO", "Alta"),
                new Task(2L, "Prueba 2", "DOING", "Baja"),
                new Task(3L, "Prueba 3", "TODO", "Media"),
                new Task(4L, "Prueba 4", "Done", "Alta")

        );
    };

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task getTaskById(Long id) {
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst().orElse(null);
    }

}
