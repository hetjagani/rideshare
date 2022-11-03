package com.rideshare.chat.service;

import com.rideshare.chat.model.Room;
import com.rideshare.chat.webentity.PaginatedEntity;

// get all the rooms by a particular user id
// create a new room
// delete a new room
// get a room by id
//
public interface IMessageService {
    PaginatedEntity<Room> getAllPaginated(Integer userId, Integer page, Integer limit) throws Exception;

    Room createRoom(Room object) throws Exception;

    boolean delete(Integer userId, Integer roomId) throws Exception;

    Room getById(Integer userId, Integer id) throws Exception;
}
