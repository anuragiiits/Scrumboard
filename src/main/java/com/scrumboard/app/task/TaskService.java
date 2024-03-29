package com.scrumboard.app.task;

import com.scrumboard.app.exception.AccessDeniedException;
import com.scrumboard.app.exception.BadRequestException;
import com.scrumboard.app.exception.ResourceNotFoundException;
import com.scrumboard.app.task.pojo.request.TaskFilterRequest;
import com.scrumboard.app.task.pojo.request.TaskRequest;
import com.scrumboard.app.task.pojo.request.TaskTitleFilterRequest;
import com.scrumboard.app.task.pojo.response.TaskResponse;
import com.scrumboard.app.task.pojo.response.TaskStatusResponse;
import com.scrumboard.app.user.ApplicationUser;
import com.scrumboard.app.user.IApplicationUserService;
import org.apache.logging.log4j.util.Strings;
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
    private IApplicationUserService applicationUserService;
    @Autowired
    private Mapper mapper;

    private Logger logger = LoggerFactory.getLogger(TaskService.class);

    /**
     * Retrieves all tasks created by the logged in user
     * Not in use now due to inappropriate format
     * @return Single List of Tasks
     */
    public List<TaskResponse> getUserTask() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<TaskResponse> tasksResponse = new ArrayList<>();
        applicationUserService.findByUsername(auth.getName()).get().getTasksBy()
                .forEach((task) -> tasksResponse.add(mapper.map(task, TaskResponse.class)));

        return tasksResponse;
    }

    /**
     * Retrieves all tasks created for the logged in user
     * @return Status category-wise lists of tasks
     */
    public TaskStatusResponse getCreatedForTask(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TaskStatusResponse taskStatusResponse = new TaskStatusResponse();
        applicationUserService.findByUsername(auth.getName()).get().getTasksFor()
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

    /**
     * Retrieves all tasks created by the logged in user
     * @return Status category-wise lists of tasks
     */
    public TaskStatusResponse getCreatedByTaskForOthers(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TaskStatusResponse taskStatusResponse = new TaskStatusResponse();
        ApplicationUser user = applicationUserService.findByUsername(auth.getName()).get();
        user.getTasksBy()
                .forEach((task) -> {
                    if(!task.getCreatedFor().getId().equals(user.getId())) {
                        if (task.getStatus() == Status.PENDING)
                            taskStatusResponse.addPendingTask(mapper.map(task, TaskResponse.class));
                        if (task.getStatus() == Status.DEVELOPMENT)
                            taskStatusResponse.addDevelopmentTask(mapper.map(task, TaskResponse.class));
                        if (task.getStatus() == Status.TESTING)
                            taskStatusResponse.addTestingTask(mapper.map(task, TaskResponse.class));
                        if (task.getStatus() == Status.PRODUCTION)
                            taskStatusResponse.addProductionTask(mapper.map(task, TaskResponse.class));
                        if (task.getStatus() == Status.REJECTED)
                            taskStatusResponse.addRejectedTask(mapper.map(task, TaskResponse.class));
                    }
                });

        return taskStatusResponse;
    }

    /**
     * Retrieves all filtered-tasks created for the logged in user based on the status-list provided as Input
     * @return Status category-wise lists of filtered-tasks
     */
    public TaskStatusResponse getFilteredUserTask(TaskFilterRequest taskFilterRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TaskStatusResponse taskStatusResponse = new TaskStatusResponse();
        taskRepository.findByCreatedForUsernameAndStatusIn(auth.getName(), taskFilterRequest.getStatusTaskFilter())
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

    /**
     * @return the task with given id
     */
    public Task getTask(Long id) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("TaskId " + id + " not found"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<ApplicationUser> user = applicationUserService.findByUsername(auth.getName());

        if(user.isPresent() && (user.get().getId().equals(task.getCreatedBy().getId()) || user.get().getId().equals(task.getCreatedFor().getId()))){
            return task;
        }
        else throw new AccessDeniedException("User is not allowed");
    }

    /**
     * Retrieves the user for whom the task is created
     * @return The ApplicationUser object if present or null
     */
    private ApplicationUser getApplicationUser(TaskRequest taskRequest) {

        if(Strings.isNotEmpty(taskRequest.getCreatedFor())){
            Optional<ApplicationUser> userOptional = applicationUserService.findByUsername(taskRequest.getCreatedFor());
            if (userOptional.isPresent()) {
                return userOptional.get();
            }
            else {
                throw  new BadRequestException("Username not found");
            }
        }
        return null;
    }

    /**
     * Add a Task in the database
     * @return The task that is added
     */
    public TaskResponse addTask(TaskRequest taskRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUser createdFor = getApplicationUser(taskRequest);

        return applicationUserService.findByUsername(auth.getName()).map(user -> {
            Task task = new Task(taskRequest.getTitle().trim(), taskRequest.getDescription().trim(), taskRequest.getStatus());
            task.setCreatedBy(user);
            task.setCreatedFor(createdFor == null ? user : createdFor);
            task = taskRepository.save(task);

            return  mapper.map(task, TaskResponse.class);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Update the contents of an existing task
     * @return The updated task
     */
    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<ApplicationUser> user = applicationUserService.findByUsername(auth.getName());

        return taskRepository.findById(taskId).map(originalTask -> {
            if(user.isPresent() && (user.get().getId().equals(originalTask.getCreatedBy().getId()) || user.get().getId().equals(originalTask.getCreatedFor().getId()))){
                originalTask.setTitle(taskRequest.getTitle().trim());
                originalTask.setDescription(taskRequest.getDescription().trim());
                originalTask.setStatus(taskRequest.getStatus());

                originalTask = taskRepository.save(originalTask);

                return mapper.map(originalTask, TaskResponse.class);
            }
            else throw new AccessDeniedException("User is not allowed");
        }).orElseThrow(() -> new ResourceNotFoundException("TaskId " + taskId + " not found"));
    }

    /**
     * A utility function to check if secondString is a substring of firstString
     * @return true or false
     */
    private static boolean containsIgnoreCase(String firstString, String secondString) {
        final int length = secondString.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(secondString.charAt(0));
        final char firstUp = Character.toUpperCase(secondString.charAt(0));

        for (int i = firstString.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = firstString.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (firstString.regionMatches(true, i, secondString, 0, length))
                return true;
        }

        return false;
    }

    /**
     * Retrieves all filtered CreatedFor tasks based on the Task title provided as Input
     * @return Status category-wise lists of filtered-tasks
     */
    public TaskStatusResponse getFilteredTitleTask(TaskTitleFilterRequest taskTitleFilterRequest){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TaskStatusResponse taskStatusResponse = new TaskStatusResponse();
        ApplicationUser user = applicationUserService.findByUsername(auth.getName()).get();
        user.getTasksFor()
                .forEach((task) -> {
                    if(containsIgnoreCase(task.getTitle(), taskTitleFilterRequest.getTitle())) {
                        if (task.getStatus() == Status.PENDING)
                            taskStatusResponse.addPendingTask(mapper.map(task, TaskResponse.class));
                        if (task.getStatus() == Status.DEVELOPMENT)
                            taskStatusResponse.addDevelopmentTask(mapper.map(task, TaskResponse.class));
                        if (task.getStatus() == Status.TESTING)
                            taskStatusResponse.addTestingTask(mapper.map(task, TaskResponse.class));
                        if (task.getStatus() == Status.PRODUCTION)
                            taskStatusResponse.addProductionTask(mapper.map(task, TaskResponse.class));
                        if (task.getStatus() == Status.REJECTED)
                            taskStatusResponse.addRejectedTask(mapper.map(task, TaskResponse.class));
                    }
                });

        return taskStatusResponse;
    }

    /**
     * Delete the task based on the given id
     */
    public void deleteTask(Long id) {

        Task task = getTask(id);
        taskRepository.delete(task);
    }
}
