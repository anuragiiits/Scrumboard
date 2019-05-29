package com.scrumboard.app.task;

import com.scrumboard.app.exception.AccessDeniedException;
import com.scrumboard.app.exception.ResourceNotFoundException;
import com.scrumboard.app.task.pojo.request.TaskFilterRequest;
import com.scrumboard.app.task.pojo.request.TaskRequest;
import com.scrumboard.app.task.pojo.response.TaskResponse;
import com.scrumboard.app.task.pojo.response.TaskStatusResponse;
import com.scrumboard.app.user.ApplicationUser;
import com.scrumboard.app.user.ApplicationUserRepository;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private Mapper mapper;

    private Logger logger = LoggerFactory.getLogger(TaskService.class);

    public List<TaskResponse> getUserTask() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<TaskResponse> tasksResponse = new ArrayList<>();
        taskRepository.findByCreatedByUsername(auth.getName())
                .forEach((task) -> tasksResponse.add(mapper.map(task, TaskResponse.class)));

        return tasksResponse;
    }

    public TaskStatusResponse getUserStatusTask(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TaskStatusResponse taskStatusResponse = new TaskStatusResponse();
        taskRepository.findByCreatedByUsername(auth.getName())
                .forEach((task) -> {
                    if(task.getStatus() == Status.PENDING)
                        taskStatusResponse.addPendingTask(mapper.map(task, TaskResponse.class));
                    if(task.getStatus() == Status.DEVELOPMENT)
                        taskStatusResponse.addDevelopmentTask(mapper.map(task, TaskResponse.class));
                    if(task.getStatus() == Status.TESTING)
                        taskStatusResponse.addTestingTask(mapper.map(task, TaskResponse.class));
                    if(task.getStatus() == Status.PRODUCTION)
                        taskStatusResponse.addProductionTask(mapper.map(task, TaskResponse.class));
                    if(task.getStatus() == Status.REJECTED)
                        taskStatusResponse.addRejectedTask(mapper.map(task, TaskResponse.class));
                });
        return taskStatusResponse;
    }

    public TaskStatusResponse getFilteredUserTask(TaskFilterRequest taskFilterRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TaskStatusResponse taskStatusResponse = new TaskStatusResponse();
        taskRepository.findByCreatedByUsernameAndStatusIn(auth.getName(), taskFilterRequest.getStatusTaskFilter())
                .forEach(task -> {
                    if(task.getStatus() == Status.PENDING)
                        taskStatusResponse.addPendingTask(mapper.map(task, TaskResponse.class));
                    if(task.getStatus() == Status.DEVELOPMENT)
                        taskStatusResponse.addDevelopmentTask(mapper.map(task, TaskResponse.class));
                    if(task.getStatus() == Status.TESTING)
                        taskStatusResponse.addTestingTask(mapper.map(task, TaskResponse.class));
                    if(task.getStatus() == Status.PRODUCTION)
                        taskStatusResponse.addProductionTask(mapper.map(task, TaskResponse.class));
                    if(task.getStatus() == Status.REJECTED)
                        taskStatusResponse.addRejectedTask(mapper.map(task, TaskResponse.class));
                });

        return taskStatusResponse;
    }

    public Task getTask(Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("TaskId " + id + " not found"));
        Optional<ApplicationUser> user = applicationUserRepository.findByUsername(auth.getName());

        if(user.isPresent() && (user.get().getId().equals(task.getCreatedBy().getId()))){
            return task;
        }
        else throw new AccessDeniedException("User is not allowed");
    }

    public TaskResponse addTask(TaskRequest taskRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return applicationUserRepository.findByUsername(auth.getName()).map(user -> {
            Task task = new Task(taskRequest.getTitle(), taskRequest.getDescription(), taskRequest.getStatus());
            task.setCreatedBy(user);
            task = taskRepository.save(task);

            return  mapper.map(task, TaskResponse.class);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<ApplicationUser> user = applicationUserRepository.findByUsername(auth.getName());

        return taskRepository.findById(taskId).map(originalTask -> {
            if(user.isPresent() && (user.get().getId().equals(originalTask.getCreatedBy().getId()))){
                originalTask.setTitle(taskRequest.getTitle());
                originalTask.setDescription(taskRequest.getDescription());
                originalTask.setStatus(taskRequest.getStatus());

                originalTask = taskRepository.save(originalTask);

                return mapper.map(originalTask, TaskResponse.class);
            }
            else throw new AccessDeniedException("User is not allowed");
        }).orElseThrow(() -> new ResourceNotFoundException("TaskId " + taskId + " not found"));
    }

    public void deleteTask(Long id) {

        Task task = getTask(id);
        taskRepository.delete(task);
    }
}
