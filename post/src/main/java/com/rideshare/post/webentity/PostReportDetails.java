package com.rideshare.post.webentity;

import com.rideshare.post.model.Post;
import com.rideshare.post.model.PostReport;
import lombok.Data;

@Data
public class PostReportDetails extends PostReport {
    private Post post;
    private User user;

    public PostReportDetails(){}

    public PostReportDetails(PostReport postReport){
        super(postReport.getId(), postReport.getUserId(), postReport.getPostId(), postReport.getReason(), postReport.getDescription());
    }

    public PostReportDetails(Integer id, Integer postId, Integer userId, String reason, String description, Post post, User user) {
        super(id, postId, userId, reason, description);
        this.post = post;
        this.user = user;
    }

    public PostReportDetails(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
