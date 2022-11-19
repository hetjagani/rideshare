package com.rideshare.chat.service;

import com.rideshare.chat.model.Message;
import com.rideshare.chat.webentity.PaginatedEntity;

public interface IMessageService {
    PaginatedEntity<Message> getAllPaginated(Integer roomId, Integer page, Integer limit) throws Exception;

    Message createMessage(com.rideshare.chat.webentity.Message object) throws Exception;

    boolean delete(Integer id) throws Exception;

    Message getById(Integer id) throws Exception;
}
