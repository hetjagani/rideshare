package com.rideshare.post.mapper;

import com.rideshare.post.model.Post;
import com.rideshare.post.webentity.PostImage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostImageMapper implements RowMapper<PostImage> {
    @Override
    public PostImage mapRow(ResultSet rs, int rowNum) throws SQLException {
        PostImage postImage = new PostImage(rs.getInt("id"), rs.getInt("post_id"), rs.getString("url"));
        return postImage;
    }
}
