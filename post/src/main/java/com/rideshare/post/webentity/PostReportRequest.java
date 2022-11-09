package com.rideshare.post.webentity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostReportRequest {
    private Integer postId;
    private Integer userId;
    private String reason;
    private String description;

    public PostReportRequest(){}
}
