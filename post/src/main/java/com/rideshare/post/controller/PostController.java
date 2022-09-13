package com.rideshare.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    @GetMapping
    public ResponseEntity getPosts() {
        return ResponseEntity.ok("Getting all the posts");
    }

}
