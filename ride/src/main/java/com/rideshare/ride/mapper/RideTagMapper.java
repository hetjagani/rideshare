package com.rideshare.ride.mapper;

import com.rideshare.ride.model.RideTag;
import com.rideshare.ride.model.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RideTagMapper implements RowMapper<RideTag> {
    @Override
    public RideTag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag(rs.getInt("id"), rs.getString("name"));
        return new RideTag(rs.getInt("ride_id"), rs.getInt("tag_id"), tag);
    }
}
