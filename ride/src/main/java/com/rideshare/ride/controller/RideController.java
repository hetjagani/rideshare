package com.rideshare.ride.controller;

import com.rideshare.ride.model.RequestStatus;
import com.rideshare.ride.security.UserPrincipal;
import com.rideshare.ride.service.IRequestService;
import com.rideshare.ride.service.IRideService;
import com.rideshare.ride.webentity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/rides")
public class RideController {

    @Autowired
    IRideService rideService;

    @Autowired
    IRequestService requestService;

    @GetMapping(path="/noOfRides/{userId}")
    public ResponseEntity<Integer> getNoOfRidesForUser(@PathVariable Integer userId) throws Exception{
        try{
            Integer noOfRides = rideService.getNoOfRides(userId);
            return ResponseEntity.ok(noOfRides);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable Integer id, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Ride ride = rideService.getById(token, id);
            return ResponseEntity.ok(ride);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<PaginatedEntity<Ride>> getAllRides(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer userId, @RequestParam(required = false) Boolean all, @RequestHeader HttpHeaders headers) throws Exception {
        PaginatedEntity<Ride> rides;
        try {
            String token = headers.get("Authorization").get(0);
            if(userId != null && userId != 0) {
                rides = rideService.searchRides(token, userId, page, limit);
            } else if(page == null && limit == null && userId == null && all != null && all) {
                rides = new PaginatedEntity<>(rideService.getAll(token), 0, 0);
            } else {
                rides = rideService.getPaginated(token, page, limit);
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(rides);
    }

    @PostMapping
    public ResponseEntity<Ride> createRide(@RequestBody com.rideshare.ride.model.Ride ride, @AuthenticationPrincipal UserPrincipal user, @RequestHeader HttpHeaders headers) throws Exception {
        Integer userId = Integer.parseInt(user.getId());
        try {
            String token = headers.get("Authorization").get(0);
            ride.setUserId(userId);
            Ride createdRide = rideService.create(token, ride);
            return ResponseEntity.ok(createdRide);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DeleteSuccess> deleteRide(@PathVariable Integer id) throws Exception {
        try {
            boolean deleted = rideService.delete(id);
            return ResponseEntity.ok(new DeleteSuccess(deleted));
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/my")
    public ResponseEntity<PaginatedEntity<MyRide>> getAllUserRides(@AuthenticationPrincipal UserPrincipal user, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit, @RequestHeader HttpHeaders headers) throws Exception {
       try {
           String token = headers.get("Authorization").get(0);
           Integer userId = Integer.parseInt(user.getId());

           // get all rides of user
           List<MyRide> userRides = rideService.getAll(token).stream().filter(r -> Objects.equals(r.getUserId(), userId)).map((Ride r) -> {
               return toMyRide(r, false);
           }).collect(Collectors.toList());

           // get all completed requests of user
           List<MyRide> userRequestedRides = requestService.getAll(token).stream()
                   .filter(r -> Objects.equals(r.getUserId(), userId) && RequestStatus.COMPLETED.equals(r.getStatus()))
                   .map((Request r) -> toMyRide(r.getRide(), true)).collect(Collectors.toList());

            for(MyRide r: userRequestedRides) {
                if (r != null) {
                    userRides.add(r);
                }
            }

           Collections.sort(userRides);

            return ResponseEntity.ok(new PaginatedEntity<>(userRides, 0, 0));
       } catch (Exception e) {
           e.printStackTrace();
           throw e;
       }
    }

    @PutMapping(path = "/{id}/start")
    public ResponseEntity<Ride> startRide(@PathVariable Integer id, @AuthenticationPrincipal UserPrincipal user, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Integer userId = Integer.parseInt(user.getId());
            Ride startedRide = rideService.startRide(token, id, userId);
            return ResponseEntity.ok(startedRide);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping(path = "/{id}/stop")
    public ResponseEntity<Ride> stopRide(@PathVariable Integer id, @AuthenticationPrincipal UserPrincipal user, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Integer userId = Integer.parseInt(user.getId());
            Ride startedRide = rideService.stopRide(token, id, userId);
            return ResponseEntity.ok(startedRide);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private MyRide toMyRide(Ride r, Boolean isPassenger) {
        MyRide myRide = new MyRide();
        myRide.setId(r.getId());
        myRide.setPostId(r.getPostId());
        myRide.setUserId(r.getUserId());
        myRide.setPricePerPerson(r.getPricePerPerson());
        myRide.setNoPassengers(r.getNoPassengers());
        myRide.setCapacity(r.getCapacity());
        myRide.setStatus(r.getStatus());
        myRide.setTags(r.getTags());
        myRide.setStartAddress(r.getStartAddress());
        myRide.setEndAddress(r.getEndAddress());
        myRide.setCreatedAt(r.getCreatedAt());
        myRide.setIsPassenger(isPassenger);
        myRide.setStartedAt(r.getStartedAt());
        myRide.setEndedAt(r.getEndedAt());
        return myRide;
    }

}
