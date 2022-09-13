package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.model.Place;
import com.rideshare.userinfo.model.User;
import com.rideshare.userinfo.security.UserPrincipal;
import com.rideshare.userinfo.service.IPlacesService;
import com.rideshare.userinfo.webentity.DeleteSuccess;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Place> createPlace(@AuthenticationPrincipal UserPrincipal userDetails, @RequestBody com.rideshare.userinfo.webentity.Place place) throws Exception {
        try {
            Integer userId = Integer.parseInt(userDetails.getId());

            Place toCreatePlace = new Place();
            toCreatePlace.setName(place.getName());
            toCreatePlace.setFirstLine(place.getFirstLine());
            toCreatePlace.setSecondLine(place.getSecondLine());
            toCreatePlace.setCity(place.getCity());
            toCreatePlace.setState(place.getState());
            toCreatePlace.setCountry(place.getCountry());
            toCreatePlace.setZipcode(place.getZipcode());
            toCreatePlace.setUserId(userId);

            Place createdPlace = placesService.create(toCreatePlace);

            return ResponseEntity.ok(createdPlace);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping(path = "/{placeId}")
    public ResponseEntity<Place> updatePlace(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody com.rideshare.userinfo.webentity.Place place, @PathVariable Integer placeId) throws Exception {
        try {
            Integer userId = Integer.parseInt(userPrincipal.getId());
            Place dbPlace = placesService.getById(userId, placeId);

            dbPlace.setName(place.getName());
            dbPlace.setFirstLine(place.getFirstLine());
            dbPlace.setSecondLine(place.getSecondLine());
            dbPlace.setCity(place.getCity());
            dbPlace.setState(place.getState());
            dbPlace.setCountry(place.getCountry());
            dbPlace.setZipcode(place.getZipcode());

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
