package com.rideshare.post.model;

import com.rideshare.post.webentity.PostImage;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

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
    private List<PostImage> imageList;

    public Post(){}

    public Post(Integer userId, String title, String description, Timestamp createdAt, Timestamp updatedAt, String type, Integer rideId, Integer noOfLikes, List<PostImage> imageList) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
        this.rideId = rideId;
        this.noOfLikes = noOfLikes;
        this.imageList = imageList;
    }

    public Post(Integer id, Integer userId, String title, String description, Timestamp createdAt, Timestamp updatedAt, String type, Integer rideId, Integer noOfLikes, List<PostImage> imageList) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
        this.rideId = rideId;
        this.noOfLikes = noOfLikes;
        this.imageList = imageList;
    }
}
