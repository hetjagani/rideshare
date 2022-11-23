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
    private Integer refId;
    private Integer noOfLikes;
    private List<PostImage> imageList;
    private UserInfo user;

    public Post(){}

    public Post(Integer userId, String title, String description, Timestamp createdAt, Timestamp updatedAt, String type, Integer refId, Integer noOfLikes, List<PostImage> imageList, UserInfo user) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
        this.refId = refId;
        this.noOfLikes = noOfLikes;
        this.imageList = imageList;
        this.user = user;
    }

    public Post(Integer id, Integer userId, String title, String description, Timestamp createdAt, Timestamp updatedAt, String type, Integer refId, Integer noOfLikes, List<PostImage> imageList, UserInfo user) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
        this.refId = refId;
        this.noOfLikes = noOfLikes;
        this.imageList = imageList;
        this.user = user;
    }
}
