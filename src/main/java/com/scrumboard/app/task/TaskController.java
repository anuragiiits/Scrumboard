package com.scrumboard.app.task;

import com.scrumboard.app.task.pojo.response.CreateTaskResponse;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private ITaskService taskService;

    @RequestMapping("/tasks")
    public List<Task> getAllTasks(){
        return taskService.getAllTasks();
    }

    @RequestMapping("/tasks/{id}")
    public Optional<Task> getTask(@PathVariable Long id){
        return taskService.getTask(id);
    }

    @RequestMapping(method=RequestMethod.POST, value="/tasks")
    public ResponseEntity<CreateTaskResponse> addTask(@RequestBody Task task){

        if(Strings.isEmpty(task.getTitle()) && Strings.isEmpty(task.getDescription())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskService.addTask(task));
    }

    @RequestMapping(method=RequestMethod.PUT, value="/tasks")
    public ResponseEntity<String> updateTask(@RequestBody Task task){

        if(Strings.isEmpty(task.getTitle()) && Strings.isEmpty(task.getDescription()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Title and Description cannot be empty.");

        Optional<Task> currentTask = getTask(task.getId());
        if(!currentTask.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        taskService.updateTask(task);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/tasks/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}
