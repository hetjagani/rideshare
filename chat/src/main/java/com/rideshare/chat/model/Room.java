package com.rideshare.chat.model;

import lombok.Data;
import java.util.Date;

import java.util.List;
import java.lang.String;

@Data
public class Room {
    private Integer id;
    private Integer initiatedBy;
    private Integer initiatedFor;

    private Date createdAt;

    public Room() {}

    public Room(Integer id, Integer initiatedBy, Integer initiatedFor, Date createdAt) {
        this.id = id;
        this.initiatedBy = initiatedBy;
        this.initiatedFor = initiatedFor;
        this.createdAt = createdAt;
    }

    public Room(Integer id, Integer initiatedBy, Integer initiatedFor) {
        this.id = id;
        this.initiatedBy = initiatedBy;
        this.initiatedFor = initiatedFor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(Integer initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public Integer getInitiatedFor() {
        return initiatedFor;
    }

    public void setInitiatedFor(Integer initiatedFor) {
        this.initiatedFor = initiatedFor;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", initiatedBy=" + initiatedBy +
                ", initiatedFor=" + initiatedFor +
                ", createdAt=" + createdAt.toString() +
                '}';
    }
}
