package com.rideshare.ride.mapper;

import com.rideshare.ride.model.RideTag;
import com.rideshare.ride.model.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RideIdMapper implements RowMapper<Integer> {
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("ride_id");
    }
}
