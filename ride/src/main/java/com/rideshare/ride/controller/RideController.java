package com.rideshare.ride.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/rides")
public class RideController {

    @GetMapping(path = "/{userId}")
    public ResponseEntity getRidesForUser(@PathVariable String userId) {

        return ResponseEntity.ok("Getting Rides for userId: "+userId);

    }

}
