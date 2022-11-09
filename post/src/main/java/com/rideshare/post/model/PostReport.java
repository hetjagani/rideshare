package com.rideshare.post.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostReport {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private String reason;
    private String description;

    public PostReport(){}
}
