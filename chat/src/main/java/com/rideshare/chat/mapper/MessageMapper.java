package com.rideshare.chat.mapper;

import com.rideshare.chat.model.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message.MessageStatus status;
        if(rs.getString("status").equals(Message.MessageStatus.DELIVERED.toString())){
            status = Message.MessageStatus.DELIVERED;
        }else{
            status = Message.MessageStatus.RECEIVED;
        }
        return new Message(rs.getInt("id"),rs.getInt("room_id"),rs.getInt("sender_id"),rs.getInt("receiver_id"),rs.getString("content"),status,rs.getTimestamp("created_at"));
    }
}
