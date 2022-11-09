package com.rideshare.post.webentity;

import lombok.Data;

@Data
public class PostImage {
    private Integer id;
    private Integer postId;
    private String url;

    public PostImage(){}

    public PostImage(Integer postId, String url) {
        this.postId = postId;
        this.url = url;
    }

    public PostImage(Integer id, Integer postId, String url) {
        this.id = id;
        this.postId = postId;
        this.url = url;
    }
}
