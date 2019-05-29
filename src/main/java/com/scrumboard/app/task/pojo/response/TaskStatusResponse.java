package com.scrumboard.app.task.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TaskStatusResponse {

    @JsonProperty("PENDING")
    private List<TaskResponse> pendingTask;
    @JsonProperty("DEVELOPMENT")
    private List<TaskResponse> developmentTask;
    @JsonProperty("TESTING")
    private List<TaskResponse> testingTask;
    @JsonProperty("PRODUCTION")
    private List<TaskResponse> productionTask;
    @JsonProperty("REJECTED")
    private List<TaskResponse> rejectedTask;

    public TaskStatusResponse(){
        this.pendingTask = new ArrayList<>();
        this.developmentTask = new ArrayList<>();
        this.testingTask = new ArrayList<>();
        this.productionTask = new ArrayList<>();
        this.rejectedTask = new ArrayList<>();
    }

    public List<TaskResponse> getPendingTask() {
        return pendingTask;
    }

    public void setPendingTask(List<TaskResponse> pendingTask) {
        this.pendingTask = pendingTask;
    }

    public List<TaskResponse> getDevelopmentTask() {
        return developmentTask;
    }

    public void setDevelopmentTask(List<TaskResponse> developmentTask) {
        this.developmentTask = developmentTask;
    }

    public List<TaskResponse> getTestingTask() {
        return testingTask;
    }

    public void setTestingTask(List<TaskResponse> testingTask) {
        this.testingTask = testingTask;
    }

    public List<TaskResponse> getProductionTask() {
        return productionTask;
    }

    public void setProductionTask(List<TaskResponse> productionTask) {
        this.productionTask = productionTask;
    }

    public List<TaskResponse> getRejectedTask() {
        return rejectedTask;
    }

    public void setRejectedTask(List<TaskResponse> rejectedTask) {
        this.rejectedTask = rejectedTask;
    }

    public void addPendingTask(TaskResponse taskResponse) {
        this.pendingTask.add(taskResponse);
    }

    public void addDevelopmentTask(TaskResponse taskResponse){
        this.developmentTask.add(taskResponse);
    }

    public void addTestingTask(TaskResponse taskResponse){
        this.testingTask.add(taskResponse);
    }

    public void addProductionTask(TaskResponse taskResponse){
        this.productionTask.add(taskResponse);
    }

    public void addRejectedTask(TaskResponse taskResponse){
        this.rejectedTask.add(taskResponse);
    }
}
