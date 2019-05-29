package com.scrumboard.app.task;

import com.scrumboard.app.task.pojo.request.TaskFilterRequest;
import com.scrumboard.app.task.pojo.request.TaskRequest;
import com.scrumboard.app.task.pojo.response.TaskResponse;
import com.scrumboard.app.task.pojo.response.TaskStatusResponse;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@CrossOrigin
@RequestMapping("/user")
@Transactional(rollbackOn = Exception.class)
public class TaskController {

    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private ITaskService taskService;

/** Not Needed for Now

    @GetMapping("/tasks")
    public List<TaskResponse> getAllTasks(){
        return taskService.getUserTask();
    }
*/

    @GetMapping("/tasks")
    public TaskStatusResponse getAllTask(){
        return taskService.getUserStatusTask();
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Long id){
        return taskService.getTask(id);
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> addTask(@RequestBody TaskRequest taskRequest) {

        if(Strings.isEmpty(taskRequest.getTitle()) && Strings.isEmpty(taskRequest.getDescription())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(taskService.addTask(taskRequest));
    }

    @PostMapping("/task-filter")
    public TaskStatusResponse getFilteredTask(@RequestBody TaskFilterRequest taskFilterRequest) {
        return taskService.getFilteredUserTask(taskFilterRequest);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable("id") Long taskId, @RequestBody TaskRequest taskRequest){

        if(Strings.isEmpty(taskRequest.getTitle()) && Strings.isEmpty(taskRequest.getDescription()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(taskId, taskRequest));
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable("id") Long taskId){
        taskService.deleteTask(taskId);
    }
}
