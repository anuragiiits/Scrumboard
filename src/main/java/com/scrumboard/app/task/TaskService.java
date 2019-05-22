package com.scrumboard.app.task;

import com.scrumboard.app.exception.AccessDeniedException;
import com.scrumboard.app.exception.ResourceNotFoundException;
import com.scrumboard.app.user.ApplicationUser;
import com.scrumboard.app.user.ApplicationUserRepository;
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

    private Logger logger = LoggerFactory.getLogger(TaskService.class);

    public List<Task> getUserTask() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Task> tasks = new ArrayList<>();
        taskRepository.findByCreatedByUsername(auth.getName()).forEach(tasks::add);
        return tasks;
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

    public Task addTask(Task task) {
//        task = taskRepository.save(task);
//        CreateTaskResponse createTaskResponse = new CreateTaskResponse();
//        createTaskResponse.setId(task.getId());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return applicationUserRepository.findByUsername(auth.getName()).map(user -> {
            task.setCreatedBy(user);
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Task updateTask(Task task) {

//        To-DO: Handle this in a better way
//        Task originalTask = taskRepository.findById(task.getId()).orElseThrow(() -> new ResourceNotFoundException("TaskId " + task.getId() + " not found"));
//        Optional<User> user = userRepository.findById(userId);
//        if(user.isPresent() && (user.get().getId() == originalTask.getCreatedBy().getId())){
//            logger.info("{}",task.toString());
//            logger.info("{}",originalTask.toString());
//            return taskRepository.save(task);
//        }
//        else throw new AccessDeniedException("User is not allowed");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<ApplicationUser> user = applicationUserRepository.findByUsername(auth.getName());
        return taskRepository.findById(task.getId()).map(originalTask -> {
            if(user.isPresent() && (user.get().getId().equals(originalTask.getCreatedBy().getId()))){
                originalTask.setTitle(task.getTitle());
                originalTask.setDescription(task.getDescription());
                originalTask.setStatus(task.getStatus());
                return taskRepository.save(originalTask);
            }
            else throw new AccessDeniedException("User is not allowed");
        }).orElseThrow(() -> new ResourceNotFoundException("TaskId " + task.getId() + " not found"));
    }

    public void deleteTask(Long id) {
        Task task = getTask(id);
        taskRepository.delete(task);
    }
}
