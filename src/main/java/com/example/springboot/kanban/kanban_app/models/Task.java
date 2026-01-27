package com.example.springboot.kanban.kanban_app.models;

public class Task implements Cloneable {
    private Long id;
    private String title;
    private String status;
    private String priority;

    public Task() {
    }

    public Task(Long id, String title, String status, String priority) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
