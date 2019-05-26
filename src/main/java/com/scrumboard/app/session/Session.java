package com.scrumboard.app.session;

import com.scrumboard.app.user.ApplicationUser;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private Boolean status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false, updatable=false)
    private ApplicationUser createdBy;

    public Session() {

    }

    public Session(Long id, String token, Boolean status, Date expiryDate, ApplicationUser createdBy) {
        this.id = id;
        this.token = token;
        this.status = status;
        this.expiryDate = expiryDate;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ApplicationUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUser createdBy) {
        this.createdBy = createdBy;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
