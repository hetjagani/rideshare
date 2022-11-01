package com.rideshare.post.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Post {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String type;
    private Integer rideId;
    private Integer noOfLikes;
}
