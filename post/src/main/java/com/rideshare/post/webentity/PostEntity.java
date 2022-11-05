package com.rideshare.post.webentity;

import lombok.Data;

import java.util.List;

@Data
public class PostEntity {
    private Integer userId;
    private String title;
    private String description;
    private String type;
    private Integer rideId;
    private Integer noOfLikes;
    private List<String> imageUrls;

    public PostEntity(){}

    public PostEntity(Integer userId, String title, String description, String type, Integer rideId, Integer noOfLikes, List<String> imageUrls) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.rideId = rideId;
        this.noOfLikes = noOfLikes;
        this.imageUrls = imageUrls;
    }
}
