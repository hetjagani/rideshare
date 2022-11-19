package com.rideshare.chat.controller;

import com.rideshare.chat.model.Room;
import com.rideshare.chat.security.UserPrincipal;
import com.rideshare.chat.service.IRoomService;
import com.rideshare.chat.webentity.DeleteSuccess;
import com.rideshare.chat.webentity.PaginatedEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/room")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    private static final Logger logger = LogManager.getLogger();

    @GetMapping
    public ResponseEntity<PaginatedEntity<Room>> getPaginated(@AuthenticationPrincipal UserPrincipal userDetails, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception {
        try {
            Integer userId = Integer.parseInt(userDetails.getId());
            PaginatedEntity<Room> roomList = roomService.getAllPaginated(userId, page, limit);
            return ResponseEntity.ok(roomList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/{roomId}")
    public ResponseEntity<Room> getRoomById(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer roomId) throws Exception {
        try {
            return ResponseEntity.ok(roomService.getById(roomId));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@AuthenticationPrincipal UserPrincipal userDetails, @RequestBody com.rideshare.chat.webentity.Room room) throws Exception {
        try {
            Integer userId = Integer.parseInt(userDetails.getId());

            Room newRoom = new Room(-1, userId, room.getInitiatedFor());
            Room created = roomService.createRoom(newRoom);

            return ResponseEntity.ok(created);
        }catch(Exception e){
            throw e;
        }
    }

    @DeleteMapping(path = "/{roomId}")
    public ResponseEntity<DeleteSuccess> deleteRoom(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer roomId) throws Exception {
        try {
            if(roomService.delete(roomId)) {
                return ResponseEntity.ok(new DeleteSuccess(true));
            }
            return ResponseEntity.ok(new DeleteSuccess(false));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
