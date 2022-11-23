package com.rideshare.ride.controller;

import com.rideshare.ride.exception.BadRequestException;
import com.rideshare.ride.exception.EntityNotFoundException;
import com.rideshare.ride.model.User;
import com.rideshare.ride.security.UserPrincipal;
import com.rideshare.ride.service.IRequestService;
import com.rideshare.ride.service.IRideService;
import com.rideshare.ride.webentity.DeleteSuccess;
import com.rideshare.ride.webentity.PaginatedEntity;
import com.rideshare.ride.webentity.Request;
import com.rideshare.ride.webentity.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Objects;

@RestController
@RequestMapping(path = "/rides/requests")
public class RequestController {

    @Autowired
    private IRequestService requestService;

    @Autowired
    private IRideService rideService;

    @GetMapping
    public ResponseEntity<PaginatedEntity<Request>> getPaginatedRequests(@AuthenticationPrincipal UserPrincipal user,  @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit, @RequestParam(required = false) String stripePaymentId, @RequestParam(required = false) String status, @RequestParam(required = false) Integer rideId, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Integer userId = Integer.parseInt(user.getId());

            String rStatus = status != null ? status.toUpperCase() : null;

            PaginatedEntity<Request> requests = requestService.searchRequests(token, userId, stripePaymentId, rStatus, rideId, page, limit);

            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/{requestId}")
    public ResponseEntity<Request> getById(@AuthenticationPrincipal UserPrincipal user, @PathVariable Integer requestId, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Integer userId = Integer.parseInt(user.getId());
            Request request = requestService.getById(token, userId, requestId);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @PostMapping
    public ResponseEntity<Request> create(@AuthenticationPrincipal UserPrincipal user, @RequestBody com.rideshare.ride.model.Request request, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Integer userId = Integer.parseInt(user.getId());

            Ride requestedRide = rideService.getById(token, request.getRideId());
            if (Objects.equals(requestedRide.getUserId(), userId)) {
                throw new BadRequestException("user cannot request his own ride");
            }

            request.setUserId(userId);

            try {
                rideService.getById(token, request.getRideId());
            } catch (EmptyResultDataAccessException e) {
                throw new EntityNotFoundException("requested ride not found");
            }

            Request createdRequest = requestService.create(token, request);

            return ResponseEntity.ok(createdRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @PutMapping(path = "/{requestId}")
    public ResponseEntity<Request> update(@AuthenticationPrincipal UserPrincipal user, @PathVariable Integer requestId, @RequestBody com.rideshare.ride.model.Request request, @RequestHeader HttpHeaders headers) throws Exception {

        try {
            String token = headers.get("Authorization").get(0);
            Integer userId = Integer.parseInt(user.getId());
            request.setId(requestId);
            request.setUserId(userId);

            Request updatedRequest = requestService.update(token, request);

            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path = "/{requestId}")
    public ResponseEntity<DeleteSuccess> delete(@AuthenticationPrincipal UserPrincipal user, @PathVariable Integer requestId) throws Exception {
        try {
            Integer userId = Integer.parseInt(user.getId());
            return ResponseEntity.ok(new DeleteSuccess(requestService.delete(requestId, userId)));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
