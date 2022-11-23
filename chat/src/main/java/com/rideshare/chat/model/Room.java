package com.rideshare.chat.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

import java.lang.String;

@Data
public class Room {
    private Integer id;
    private Integer initiatedBy;
    private Integer initiatedFor;

    private Timestamp createdAt;

    public Room() {}

    public Room(Integer id, Integer initiatedBy, Integer initiatedFor, Timestamp createdAt) {
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

    public Timestamp getCreatedAt() {
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
