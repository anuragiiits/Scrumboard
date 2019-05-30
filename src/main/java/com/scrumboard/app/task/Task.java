package com.scrumboard.app.task;

import com.scrumboard.app.user.ApplicationUser;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    /**
     * TODO: Add a source field

        @Enumerated(EnumType.ORDINAL)
        private RequestSource source;
     */

    @ManyToOne
    @JoinColumn(nullable=false, updatable=false)
    private ApplicationUser createdBy;

    @ManyToOne
    @JoinColumn(nullable=false)
    private ApplicationUser createdFor;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Task() {
    }

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(Long id, String title, String description, Status status, Date createdAt, Date updatedAt, ApplicationUser createdBy, ApplicationUser createdFor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.createdFor = createdFor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ApplicationUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUser createdBy) {
        this.createdBy = createdBy;
    }

    public ApplicationUser getCreatedFor() {
        return createdFor;
    }

    public void setCreatedFor(ApplicationUser createdFor) {
        this.createdFor = createdFor;
    }
}
