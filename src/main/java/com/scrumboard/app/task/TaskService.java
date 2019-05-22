package com.scrumboard.app.task;

import com.scrumboard.app.task.pojo.response.CreateTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        taskRepository.findAll().forEach(tasks::add);
        return tasks;
    }

    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }

    public CreateTaskResponse addTask(Task task) {
        task = taskRepository.save(task);
        CreateTaskResponse createTaskResponse = new CreateTaskResponse();
        createTaskResponse.setId(task.getId());
        return createTaskResponse;
    }

    public void updateTask(Task task) {
        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
