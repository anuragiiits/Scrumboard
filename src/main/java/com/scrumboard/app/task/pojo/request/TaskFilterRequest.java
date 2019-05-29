package com.scrumboard.app.task.pojo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scrumboard.app.task.Status;

import java.util.List;

public class TaskFilterRequest {

    @JsonProperty("status")
    private List<Status> statusTaskFilter;

    public List<Status> getStatusTaskFilter() {
        return statusTaskFilter;
    }

    public void setStatusTaskFilter(List<Status> statusTaskFilter) {
        this.statusTaskFilter = statusTaskFilter;
    }

}
