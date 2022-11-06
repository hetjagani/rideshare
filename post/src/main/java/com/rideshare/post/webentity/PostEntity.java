package com.rideshare.post.webentity;

import lombok.Data;

import java.util.List;

@Data
public class PostEntity {
    private Integer userId;
    private String title;
    private String description;
    private String type;
    private Integer refId;
    private Integer noOfLikes;
    private List<String> imageUrls;

    public PostEntity(){}

    public PostEntity(Integer userId, String title, String description, String type, Integer refId, Integer noOfLikes, List<String> imageUrls) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.refId = refId;
        this.noOfLikes = noOfLikes;
        this.imageUrls = imageUrls;
    }
}
