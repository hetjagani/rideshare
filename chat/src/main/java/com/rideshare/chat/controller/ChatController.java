package com.rideshare.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/chats")
public class ChatController {

    @GetMapping(path = "/{userId}")
    public ResponseEntity getChatsForUser(@PathVariable String userId) {

        return ResponseEntity.ok("Getting Chats for userId: "+userId);

    }

}
