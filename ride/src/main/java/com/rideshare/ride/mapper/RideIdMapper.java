package com.rideshare.ride.mapper;

import com.rideshare.ride.model.RideTag;
import com.rideshare.ride.model.Tag;
import com.rideshare.ride.webentity.RideRating;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RideIdMapper implements RowMapper<RideRating> {
    @Override
    public RideRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RideRating(rs.getInt("ride_id"), rs.getInt("rating_id"));
    }
}
