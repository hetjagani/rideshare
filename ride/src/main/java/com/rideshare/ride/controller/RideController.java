package com.rideshare.ride.controller;

import com.rideshare.ride.model.RequestStatus;
import com.rideshare.ride.security.UserPrincipal;
import com.rideshare.ride.service.IRequestService;
import com.rideshare.ride.service.IRideService;
import com.rideshare.ride.webentity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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

           List<RideRating> userRidesRated = rideService.checkRideForUserIfRated(userId);
           HashMap<Integer,Integer> userRidesRatedSet = new HashMap<>();

           for(RideRating rr : userRidesRated){
                userRidesRatedSet.put(rr.getRideId(),rr.getRatingId());
           }

           Map<Integer, List<Request>> requestMap = requestService.getCompletedRequests(token);
           log.info(requestMap.toString());

           // get all rides of user
           List<MyRide> userRides = rideService.getAll(token).stream().filter(r -> Objects.equals(r.getUserId(), userId)).map((Ride r) -> {
               return toMyRide(r, false, false,-1, requestMap.get(r.getId()));
           }).collect(Collectors.toList());

           // get all completed requests of user
           List<MyRide> userRequestedRides = requestService.getAll(token).stream()
                   .filter(r -> Objects.equals(r.getUserId(), userId) && RequestStatus.COMPLETED.equals(r.getStatus()))
                   .map((Request r) -> toMyRide(r.getRide(), true, false, -1, requestMap.get(r.getRideId()))).collect(Collectors.toList());

            for(MyRide r: userRequestedRides) {
                if (r != null) {
                    r.setIsRatedByUser(userRidesRatedSet.containsKey(r.getId()) ? true : false);
                    r.setRatingsId(userRidesRatedSet.get(r.getId()));
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

    @PostMapping(path="/{id}/ratings/{rId}")
    public ResponseEntity<Integer> createRideRatingForUser(@PathVariable Integer id, @PathVariable Integer rId, @AuthenticationPrincipal UserPrincipal user, @RequestHeader HttpHeaders headers) throws Exception {
        try{
            Integer userId = Integer.parseInt(user.getId());
            Integer createdId = rideService.createRideRatingForUser(id, userId, rId);
            return ResponseEntity.ok(createdId);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    private MyRide toMyRide(Ride r, Boolean isPassenger, boolean isRated, Integer ratingsId, List<Request> requests) {
        MyRide myRide = new MyRide();
        myRide.setId(r.getId());
        myRide.setPostId(r.getPostId());
        myRide.setUserId(r.getUserId());
        myRide.setRideTime(r.getRideTime());
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
        myRide.setIsRatedByUser(isRated);
        myRide.setRatingsId(ratingsId);
        myRide.setRequests(requests);
        return myRide;
    }
}
