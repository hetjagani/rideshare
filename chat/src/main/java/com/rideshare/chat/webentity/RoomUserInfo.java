package com.rideshare.chat.webentity;

import java.sql.Timestamp;
import java.util.Date;

public class RoomUserInfo {
    private Integer roomId;
    private Integer userId;
    private String profileImage;
    private String name;

    private String last;

    private Timestamp createdAt;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public RoomUserInfo(Integer roomId, Integer userId, String profileImage, String name, String last, Timestamp createdAt) {
        this.roomId = roomId;
        this.userId = userId;
        this.profileImage = profileImage;
        this.name = name;
        this.last = last;
        this.createdAt = createdAt;
    }

    public RoomUserInfo(Integer roomId, Integer userId, String profileImage, String name, String last) {
        this.roomId = roomId;
        this.userId = userId;
        this.profileImage = profileImage;
        this.name = name;
        this.last = last;
        this.createdAt = new Timestamp(0);
    }

    public Integer getRoomId() {
        return roomId;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
