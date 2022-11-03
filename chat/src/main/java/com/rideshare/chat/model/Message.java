package com.rideshare.chat.model;

import lombok.Data;

import java.util.Date;
import java.lang.String;


@Data
public class Message {

    public enum MessageStatus{
        RECEIVED, DELIVERED
    }
    private Integer id;
    private Integer roomId;
    private Integer senderId;
    private Integer receiverId;
    private String content;

    private MessageStatus status;

    private Date createdAt;

    public Message() {}

    public Message(Integer id, Integer roomId, Integer senderId, Integer receiverId, String content, MessageStatus status) {
        this.id = id;
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public com.rideshare.chat.model.Message.MessageStatus getStatus() {
        return status;
    }

    public void setStatus(com.rideshare.chat.model.Message.MessageStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt.toString() +
                '}';
    }
}
