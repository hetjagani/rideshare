package com.rideshare.chat.mapper;

import com.rideshare.chat.model.Room;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomMapper implements RowMapper<Room> {
    @Override
    public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Room(rs.getInt("id"), rs.getInt("initiated_by"), rs.getInt("initiated_for"),rs.getTimestamp("created_at"));
    }
}
