package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.model.Place;
import com.rideshare.userinfo.model.User;
import com.rideshare.userinfo.security.UserPrincipal;
import com.rideshare.userinfo.service.IPlacesService;
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

}
