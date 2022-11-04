package com.rideshare.chat.service;

import com.rideshare.chat.mapper.MessageMapper;
import com.rideshare.chat.model.Message;
import com.rideshare.chat.util.Pagination;
import com.rideshare.chat.webentity.PaginatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

class MessageService implements IMessageService{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public PaginatedEntity<Message> getAllPaginated(Integer roomId, Integer page, Integer limit) throws Exception {
        Integer offset = Pagination.getOffset(page,limit);
        String query = "SELECT * FROM \"chat\".\"message\" WHERE roomId = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Message> messageList = jdbcTemplate.query(query, new MessageMapper(), roomId, limit, offset);
        return new PaginatedEntity<>(messageList, page, limit);
    }

    @Override
    public Message getById(Integer id) throws Exception {
        String query = "SELECT * FROM \"chat\".\"message\" WHERE id = ? ";
        Message message = jdbcTemplate.queryForObject(query, new MessageMapper(), id);
        return message;
    }

    @Override
    public Message createMessage(Message object) throws Exception {
        String query = "INSERT INTO \"chat\".\"message\"(room_id, sender_id, receiver_id, status, content) VALUES (?, ?, ?, ?, ?) RETURNING id;";
        Integer id = jdbcTemplate.queryForObject(query, Integer.class, object.getRoomId(), object.getSenderId(), object.getReceiverId(), object.getStatus(), object.getContent());
        Message createdMessage = getById(id);
        return createdMessage;
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String query = "DELETE FROM chat.message\n" +
                "\tWHERE id=?";

        int affectedRows = jdbcTemplate.update(query, id);
        return affectedRows != 0;
    }

}