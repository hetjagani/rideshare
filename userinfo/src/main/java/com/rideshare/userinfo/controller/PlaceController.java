package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.exception.ForbiddenException;
import com.rideshare.userinfo.facade.RideServiceFacade;
import com.rideshare.userinfo.model.Place;
import com.rideshare.userinfo.model.User;
import com.rideshare.userinfo.security.UserPrincipal;
import com.rideshare.userinfo.service.IPlacesService;
import com.rideshare.userinfo.webentity.DeleteSuccess;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users/places")
@PreAuthorize("hasAuthority('DRIVER') or hasAuthority('RIDER')")
public class PlaceController {

    @Autowired
    private IPlacesService placesService;

    @Autowired
    private RideServiceFacade rideService;

    private Logger logger = LogManager.getLogger();

    @GetMapping
    public ResponseEntity<PaginatedEntity<Place>> getPaginated(@AuthenticationPrincipal UserPrincipal userDetails, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception {
       try {
           Integer userId = Integer.parseInt(userDetails.getId());
           PaginatedEntity<Place> placeList = placesService.getAllPaginated(userId, page, limit);
           return ResponseEntity.ok(placeList);
       } catch (Exception e) {
           e.printStackTrace();
           throw e;
       }
    }

    @GetMapping(path = "/{placeId}")
    public ResponseEntity<Place> getPlaceById(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer placeId) throws Exception {
        try {
            Integer userId = Integer.parseInt(userPrincipal.getId());

            return ResponseEntity.ok(placesService.getById(userId, placeId));
        } catch (Exception e) {
           e.printStackTrace();
           throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestHeader HttpHeaders headers, @AuthenticationPrincipal UserPrincipal userDetails, @RequestBody com.rideshare.userinfo.webentity.Place place) throws Exception {

        Integer userId = Integer.parseInt(userDetails.getId());
        String token = headers.get("Authorization").get(0);

        Integer addressId = place.getAddressId();

        //Checks if address is present with Address Id provided. Throws Exception if not found.
        rideService.checkIfAddressIsValid(token, addressId);

        Place toCreatePlace = new Place();
        toCreatePlace.setName(place.getName());
        toCreatePlace.setAddressId(addressId);
        toCreatePlace.setUserId(userId);

        Place createdPlace = placesService.create(toCreatePlace);

        return ResponseEntity.ok(createdPlace);
    }

    @PutMapping(path = "/{placeId}")
    public ResponseEntity<Place> updatePlace(@RequestHeader HttpHeaders headers, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody com.rideshare.userinfo.webentity.Place place, @PathVariable Integer placeId) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Integer userId = Integer.parseInt(userPrincipal.getId());
            Place dbPlace = placesService.getById(userId, placeId);

            Integer addressId = place.getAddressId();

            //Checks if address is present with Address Id provided. Throws Exception if not found.
            rideService.checkIfAddressIsValid(token, addressId);

            dbPlace.setName(place.getName());
            dbPlace.setAddressId(place.getAddressId());
            dbPlace.setUserId(place.getUserId());

            Place updatedPlace = placesService.update(userId, dbPlace.getId(), dbPlace);

            return ResponseEntity.ok(updatedPlace);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path = "/{placeId}")
    public ResponseEntity<DeleteSuccess> deletePlace(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer placeId) throws Exception {
        try {
            Integer userId = Integer.parseInt(userPrincipal.getId());

            if(placesService.delete(userId, placeId)) {
                return ResponseEntity.ok(new DeleteSuccess(true));
            }
            return ResponseEntity.ok(new DeleteSuccess(false));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
