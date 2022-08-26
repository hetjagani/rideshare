package com.rideshare.rating.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/ratings")
public class RatingController {

    @GetMapping(path = "/{userId}")
    public ResponseEntity getRatingsForUser(@PathVariable String userId) {

        return ResponseEntity.ok("Getting Ratings for userId: "+userId);

    }

}
