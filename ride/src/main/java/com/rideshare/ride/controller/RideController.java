package com.rideshare.ride.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/rides")
public class RideController {

    @GetMapping(path = "/{id}")
    public ResponseEntity getRidesForUser(@PathParam("id") String userId) {

        return ResponseEntity.ok("Getting Rides for userId: "+userId);

    }

}
