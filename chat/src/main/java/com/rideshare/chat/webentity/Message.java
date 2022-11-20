package com.rideshare.chat.webentity;

import lombok.Data;

import java.util.Date;
import java.lang.String;

public class Message {

    private Integer roomId;
    private Integer senderId;
    private Integer receiverId;
    private String content;

    public Message() {}

    public Message(Integer roomId, Integer senderId, Integer receiverId, String content) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
