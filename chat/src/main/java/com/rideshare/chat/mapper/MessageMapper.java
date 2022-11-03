package com.rideshare.chat.mapper;

import com.rideshare.chat.model.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Message(rs.getInt("id"), rs.getInt("initiatedBy"), rs.getInt("initiatedFor"), rs.getTimestamp("createdAt"));
    }
}
