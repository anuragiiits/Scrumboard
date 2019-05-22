package com.scrumboard.app.task;

import com.scrumboard.app.task.pojo.response.CreateTaskResponse;

import java.util.List;
import java.util.Optional;

public interface ITaskService {
    CreateTaskResponse addTask(Task task);
    void updateTask(Task task);
    void deleteTask(Long id);
    Optional<Task> getTask(Long id);
    List<Task> getAllTasks();
}
