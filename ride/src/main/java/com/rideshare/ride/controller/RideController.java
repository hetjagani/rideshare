package com.rideshare.ride.controller;

import com.rideshare.ride.security.UserPrincipal;
import com.rideshare.ride.service.IRideService;
import com.rideshare.ride.webentity.DeleteSuccess;
import com.rideshare.ride.webentity.PaginatedEntity;
import com.rideshare.ride.webentity.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/rides")
public class RideController {

    @Autowired
    IRideService rideService;


    @GetMapping(path = "/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable Integer id) throws Exception {
        try {
            Ride ride = rideService.getById(id);
            return ResponseEntity.ok(ride);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<PaginatedEntity<Ride>> getAllRides(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer userId, @RequestParam(required = false) Boolean all) throws Exception {
        PaginatedEntity<Ride> rides;
        try {
            if(userId != null && userId != 0) {
                rides = rideService.searchRides(userId, page, limit);
            } else if(page == null && limit == null && userId == null && all != null && all) {
                rides = new PaginatedEntity<>(rideService.getAll(), 0, 0);
            } else {
                rides = rideService.getPaginated(page, limit);
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(rides);
    }

    @PostMapping
    public ResponseEntity<Ride> createRide(@RequestBody com.rideshare.ride.model.Ride ride, @AuthenticationPrincipal UserPrincipal user) throws Exception {
        Integer userId = Integer.parseInt(user.getId());
        try {
            ride.setUserId(userId);
            Ride createdRide = rideService.create(ride);
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

}
