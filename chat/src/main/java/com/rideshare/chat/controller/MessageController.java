package com.rideshare.chat.controller;

import com.rideshare.chat.model.Message;
import com.rideshare.chat.security.UserPrincipal;
import com.rideshare.chat.service.IMessageService;
import com.rideshare.chat.webentity.DeleteSuccess;
import com.rideshare.chat.webentity.PaginatedEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LogManager.getLogger();

    @MessageMapping("/chat")
    public void processMessage(@Payload com.rideshare.chat.webentity.Message message) throws Exception {
        try{
            Message newMessage = messageService.createMessage(message);

            messagingTemplate.convertAndSendToUser(newMessage.getReceiverId().toString(),"/queue/messages", newMessage);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @GetMapping(path = "/rooms/{roomId}/messages")
    public ResponseEntity<PaginatedEntity<Message>> getPaginated(@AuthenticationPrincipal UserPrincipal userDetails, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit, @PathVariable Integer roomId) throws Exception {
        try {
            PaginatedEntity<Message> roomList = messageService.getAllPaginated(roomId, page, limit);
            return ResponseEntity.ok(roomList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer roomId) throws Exception {
        try {
            return ResponseEntity.ok(messageService.getById(roomId));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path = "/messages/{messageId}")
    public ResponseEntity<DeleteSuccess> deleteMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer messageId) throws Exception {
        try {
            if(messageService.delete(messageId)) {
                return ResponseEntity.ok(new DeleteSuccess(true));
            }
            return ResponseEntity.ok(new DeleteSuccess(false));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
