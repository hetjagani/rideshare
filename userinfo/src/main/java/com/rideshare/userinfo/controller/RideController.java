package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.security.UserPrincipal;
import com.rideshare.userinfo.service.RideService;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import com.rideshare.userinfo.webentity.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;


@RestController
@RequestMapping(path = "/users/{userId}/rides")
public class RideController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RideService rideService;

    @GetMapping
    public ResponseEntity<PaginatedEntity<Ride>> getAllRides(@RequestHeader HttpHeaders headers,
                                                             @AuthenticationPrincipal UserPrincipal userDetails,
                                                             @RequestParam(required = false) Integer page,
                                                             @RequestParam(required = false) Integer limit,
                                                             @PathVariable Integer userId) throws Exception {
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        String token = headers.get("Authorization").get(0);
        Boolean isAdmin = false;

        for(GrantedAuthority s: roles){
            if(s.toString().equals("ADMIN")){
              isAdmin = true;
              break;
          };
        }

        try {
            PaginatedEntity<Ride> rides = rideService.getAllPaginated(token, page, limit, String.valueOf(userDetails.getId()), isAdmin);
            return ResponseEntity.ok(rides);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}