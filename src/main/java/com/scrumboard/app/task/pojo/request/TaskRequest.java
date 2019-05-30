package com.scrumboard.app.task.pojo.request;

import com.scrumboard.app.task.Status;

public class TaskRequest {

    private String title;
    private String description;
    private Status status;
    private String createdFor;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCreatedFor() {
        return createdFor;
    }

    public void setCreatedFor(String createdFor) {
        this.createdFor = createdFor;
    }
}
