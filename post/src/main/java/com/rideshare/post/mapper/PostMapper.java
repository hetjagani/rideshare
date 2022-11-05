package com.rideshare.post.mapper;

import com.rideshare.post.model.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("description"), rs.getTimestamp("created_at"), rs.getTimestamp("updated_at"), rs.getString("type"), rs.getInt("ride_id"), rs.getInt("no_of_likes"), null);
        return post;
    }
}
