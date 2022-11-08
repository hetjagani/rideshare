package com.rideshare.post.mapper;

import com.rideshare.post.model.Post;
import com.rideshare.post.model.PostReport;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostReportMapper implements RowMapper<PostReport> {
    @Override
    public PostReport mapRow(ResultSet rs, int rowNum) throws SQLException {
        PostReport postReport = new PostReport(rs.getInt("id"), rs.getInt("post_id"), rs.getInt("user_id"), rs.getString("reason"), rs.getString("description"));
        return postReport;
    }
}
