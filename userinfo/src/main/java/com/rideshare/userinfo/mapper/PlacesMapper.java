package com.rideshare.userinfo.mapper;

import com.rideshare.userinfo.model.Place;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlacesMapper implements RowMapper<Place> {
    @Override
    public Place mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Place(rs.getInt("id"), rs.getString("name"), rs.getString("first_line"), rs.getString("second_line"), rs.getString("city"), rs.getString("state"), rs.getString("country"), rs.getString("zipcode"), rs.getInt("user_id"));
    }
}
