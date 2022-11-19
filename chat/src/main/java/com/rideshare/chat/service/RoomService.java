package com.rideshare.chat.service;

import com.rideshare.chat.model.Room;
import com.rideshare.chat.util.Pagination;
import com.rideshare.chat.webentity.PaginatedEntity;
import com.rideshare.chat.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
class RoomService implements IRoomService{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public PaginatedEntity<Room> getAllPaginated(Integer userId, Integer page, Integer limit) throws Exception {
        Integer offset = Pagination.getOffset(page,limit);
        String query = "SELECT * FROM \"chat\".\"room\" WHERE initiated_by = ? OR initiated_for = ? LIMIT ? OFFSET ?";
        List<Room> roomListForUser = jdbcTemplate.query(query, new RoomMapper(), userId, userId, limit, offset);
        return new PaginatedEntity<Room>(roomListForUser, page, limit);
    }

    @Override
    public Room getById(Integer id) throws Exception {
        String query = "SELECT * FROM \"chat\".\"room\" WHERE id = ? ";
        Room room = jdbcTemplate.queryForObject(query, new RoomMapper(), id);
        return room;
    }

    @Override
    public Room createRoom(Room object) throws Exception {
        String query = "INSERT INTO \"chat\".\"room\"(initiated_by, initiated_for) VALUES (?, ?) RETURNING id;";
        Integer id = jdbcTemplate.queryForObject(query, Integer.class, object.getInitiatedBy(), object.getInitiatedFor());
        Room createdRoom = getById(id);
        return createdRoom;
    }

    @Override
    public boolean delete(Integer roomId) throws Exception {
        String query = "DELETE FROM chat.room\n" +
                "\tWHERE id=?";

        int affectedRows = jdbcTemplate.update(query, roomId);
        return affectedRows != 0;
    }

}