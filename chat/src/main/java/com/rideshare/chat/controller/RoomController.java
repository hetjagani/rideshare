package com.rideshare.chat.controller;

import com.rideshare.chat.facade.UserInfoFacade;
import com.rideshare.chat.model.Message;
import com.rideshare.chat.model.Room;
import com.rideshare.chat.security.UserPrincipal;
import com.rideshare.chat.service.IMessageService;
import com.rideshare.chat.service.IRoomService;
import com.rideshare.chat.webentity.DeleteSuccess;
import com.rideshare.chat.webentity.PaginatedEntity;
import com.rideshare.chat.webentity.RoomUserInfo;
import com.rideshare.chat.webentity.UserInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/rooms")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @Autowired
    private UserInfoFacade userInfoFacade;

    @Autowired
    private IMessageService messageService;

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

    @GetMapping(path = "/users")
    public ResponseEntity<PaginatedEntity<RoomUserInfo>> getUserRooms(@AuthenticationPrincipal UserPrincipal userDetails, @RequestHeader HttpHeaders headers, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception{
        try{
            List<UserInfo> userInfoList = userInfoFacade.getAllUsers(headers.get("Authorization").get(0));
            List<Room> getRooms = roomService.getAllPaginated(Integer.parseInt(userDetails.getId()),page,limit).getNodes();
            List<Message> getLastMessages = messageService.getLastMessages();

            HashMap<Integer,Message> last = new HashMap<>();
            for(Message m : getLastMessages){
                last.put(m.getRoomId(),m);
            }

            HashMap<Integer,UserInfo> map = new HashMap<>();
            for(UserInfo usr : userInfoList){
                map.put(usr.getId(),usr);
            }

            List<RoomUserInfo> lsRoomUser = new ArrayList<>();
            for(Room r : getRooms){
                Integer id = Integer.parseInt(userDetails.getId());
                Integer oth = id == r.getInitiatedBy() ? r.getInitiatedFor() : r.getInitiatedBy();
                String name = map.get(oth).getFirstName()+" "+map.get(oth).getLastName();
                String lMsg="No Chats Yet";
                if(last.containsKey(r.getId())){
                    lMsg = last.get(r.getId()).getSenderId() == oth ? name+": "+last.get(r.getId()).getContent() : "You: "+last.get(r.getId()).getContent();
                    lsRoomUser.add(new RoomUserInfo(r.getId(),oth,map.get(oth).getProfileImage(),name, lMsg, last.get(r.getId()).getCreatedAt()));
                }else{
                    lsRoomUser.add(new RoomUserInfo(r.getId(),oth,map.get(oth).getProfileImage(),name, lMsg));
                }
            }
            return ResponseEntity.ok(new PaginatedEntity<>(lsRoomUser,page,limit));
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
