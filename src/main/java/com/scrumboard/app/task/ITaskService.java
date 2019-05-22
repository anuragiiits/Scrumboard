package com.scrumboard.app.task;

import com.scrumboard.app.task.pojo.response.CreateTaskResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITaskService {
    Task addTask(Task task);
    Task updateTask(Task task);
    void deleteTask(Long id);
    Task getTask(Long id);
    List<Task> getUserTask();
}
