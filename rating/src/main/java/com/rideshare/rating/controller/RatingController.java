package com.rideshare.rating.controller;

import com.rideshare.rating.model.Rating;
import com.rideshare.rating.security.UserPrincipal;
import com.rideshare.rating.service.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import javax.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/ratings")
public class RatingController {


    @Value("${app.userinfo.url}")
    private String userinfoUrl;
    @Autowired
    private IRatingService ratingService;

    @Autowired
    private RestTemplate restTemplate;
    @GetMapping(path = "/{userId}")
    public ResponseEntity getRatingsForUser(@PathVariable String userId) {
        return ResponseEntity.ok("Getting Ratings for userId: "+userId);
    }

    @PostMapping
    public ResponseEntity<Rating> createRating(@RequestHeader HttpHeaders headers, @RequestBody Rating rating, @AuthenticationPrincipal UserPrincipal userDetails) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);

            // Check if User exists with user_id
            String requestURL = userinfoUrl + "/users/" +rating.getUserId();
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", token);

            HttpEntity request = new HttpEntity(header);
            try {
                ResponseEntity<String> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, String.class);
            } catch (Exception e){
                e.printStackTrace();
                throw new Exception("User Does not exist");
            }

            Rating newRating = ratingService.create(rating);
            return ResponseEntity.ok(newRating);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
