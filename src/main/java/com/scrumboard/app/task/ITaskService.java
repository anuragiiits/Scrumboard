package com.scrumboard.app.task;


import com.scrumboard.app.task.pojo.request.TaskFilterRequest;
import com.scrumboard.app.task.pojo.request.TaskRequest;
import com.scrumboard.app.task.pojo.request.TaskTitleFilterRequest;
import com.scrumboard.app.task.pojo.response.TaskResponse;
import com.scrumboard.app.task.pojo.response.TaskStatusResponse;

import java.util.List;

public interface ITaskService {
    TaskResponse addTask(TaskRequest taskRequest);
    TaskResponse updateTask(Long taskId, TaskRequest task);
    void deleteTask(Long id);
    Task getTask(Long id);
    List<TaskResponse> getUserTask();
    TaskStatusResponse getCreatedForTask();
    TaskStatusResponse getCreatedByTaskForOthers();
    TaskStatusResponse getFilteredUserTask(TaskFilterRequest taskFilterRequest);
    TaskStatusResponse getFilteredTitleTask(TaskTitleFilterRequest taskTitleFilterRequest);
}
