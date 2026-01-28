package com.example.springboot.kanban.kanban_app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    public Task() {
    }

    public Task(Long id, String title, TaskStatus status, TaskPriority priority) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String toString() {
        return "{id=" + id + ", title='" + title + "', status=" + status + ", priority=" + priority + "}";
    }

    public enum TaskStatus {
        TODO, DOING, DONE
    }

    public enum TaskPriority {
        Baja, Media, Alta
    }

    @Override
    public Object clone() {
        try {
            return super.clone();

        } catch (CloneNotSupportedException e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
    }

}
