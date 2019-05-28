package com.scrumboard.app.task;


import com.scrumboard.app.task.pojo.request.TaskRequest;
import com.scrumboard.app.task.pojo.response.TaskResponse;

import java.util.List;

public interface ITaskService {
    TaskResponse addTask(TaskRequest taskRequest);
    TaskResponse updateTask(Long taskId, TaskRequest task);
    void deleteTask(Long id);
    Task getTask(Long id);
    List<TaskResponse> getUserTask();
}
