package com.scrumboard.app.task.pojo.request;

import com.scrumboard.app.task.Source;

public class TaskTitleFilterRequest {

    private String title;
    private Source source;          //To be implemented

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
