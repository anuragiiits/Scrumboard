package com.scrumboard.app.task;

import com.scrumboard.app.task.pojo.response.CreateTaskResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITaskService {
    Task addTask(Long userId, Task task);
    Task updateTask(Long userId, Task task);
    void deleteTask(Long userId, Long id);
    Task getTask(Long userId, Long id);
    List<Task> getTaskByUserId(Long userId);
}
