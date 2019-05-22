package com.scrumboard.app.task;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class TaskController {

    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private ITaskService taskService;

    @GetMapping("{userId}/tasks")
    public List<Task> getAllTasks(@PathVariable Long userId){
        return taskService.getTaskByUserId(userId);
    }

    @GetMapping("{userId}/tasks/{id}")
    public Task getTask(@PathVariable Long userId, @PathVariable Long id){
        return taskService.getTask(userId, id);
    }

    @PostMapping("{userId}/tasks")
    public ResponseEntity<Task> addTask(@PathVariable Long userId, @RequestBody Task task){

        if(Strings.isEmpty(task.getTitle()) && Strings.isEmpty(task.getDescription())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskService.addTask(userId, task));
    }

    @PutMapping("{userId}/tasks")
    public ResponseEntity<Task> updateTask(@PathVariable Long userId, @RequestBody Task task){

        if(Strings.isEmpty(task.getTitle()) && Strings.isEmpty(task.getDescription()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskService.updateTask(userId, task));
    }

    @DeleteMapping("{userId}/tasks/{id}")
    public void deleteTask(@PathVariable Long userId, @PathVariable Long id){
        taskService.deleteTask(userId, id);
    }
}
