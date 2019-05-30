package com.scrumboard.app.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scrumboard.app.session.Session;
import com.scrumboard.app.task.Task;

import javax.persistence.*;
import java.util.Set;

@Entity
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String username;

    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "createdFor", cascade = CascadeType.ALL)
    private Set<Task> tasksFor;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private Set<Task> tasksBy;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private Set<Session> sessions;

    public ApplicationUser() {
    }

    public ApplicationUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ApplicationUser(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Task> getTasksFor() {
        return tasksFor;
    }

    public void setTasksFor(Set<Task> tasksFor) {
        this.tasksFor = tasksFor;
    }

    public Set<Task> getTasksBy() {
        return tasksBy;
    }

    public void setTasksBy(Set<Task> tasksBy) {
        this.tasksBy = tasksBy;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }
}
