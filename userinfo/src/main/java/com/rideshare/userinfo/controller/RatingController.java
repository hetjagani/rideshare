package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.model.UserInfo;
import com.rideshare.userinfo.security.UserPrincipal;
import com.rideshare.userinfo.service.RatingService;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import com.rideshare.userinfo.webentity.Rating;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RestTemplate restTemplate;

    private final Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<PaginatedEntity<Rating>> getAllRatingsForUser(@RequestHeader HttpHeaders headers,
                                                                        @RequestParam(required = false) Integer page,
                                                                        @RequestParam(required = false) Integer limit,
                                                                        @PathVariable Integer userId,
                                                                        @AuthenticationPrincipal UserPrincipal userDetails)
            throws Exception {
        try {
            Boolean all = false;
            Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
            String token = headers.get("Authorization").get(0);
            Boolean isAdmin = false;

            for(GrantedAuthority s: roles){
                if(s.toString().equals("ADMIN")){
                    isAdmin = true;
                    break;
                };
            }

            if(isAdmin == false){
                if(Integer.parseInt(userDetails.getId()) != userId){
                    throw new Exception("Cannot Request Ratings for Other Users");
                }
            }

            PaginatedEntity<Rating> userInfoList = ratingService.getAllPaginated(token, page, limit, userId, isAdmin);
            return ResponseEntity.ok(userInfoList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Rating> createRating(@RequestBody com.rideshare.userinfo.model.Rating rating,
                                               @RequestHeader HttpHeaders headers,
                                               @PathVariable Integer userId,
                                               @AuthenticationPrincipal UserPrincipal userDetails) throws Exception {

        String token = headers.get("Authorization").get(0);
        Rating response = ratingService.create(token, rating);
        return ResponseEntity.ok(response);
    }
}
