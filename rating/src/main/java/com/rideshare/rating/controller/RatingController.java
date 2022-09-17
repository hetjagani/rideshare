package com.rideshare.rating.controller;

import com.rideshare.rating.model.Rating;
import com.rideshare.rating.security.UserPrincipal;
import com.rideshare.rating.service.IRatingService;
import com.rideshare.rating.webentity.DeleteSuccess;
import com.rideshare.rating.webentity.PaginatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "/ratings")
public class RatingController {
    @Value("${app.userinfo.url}")
    private String userinfoUrl;
    @Autowired
    private IRatingService ratingService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<PaginatedEntity<com.rideshare.rating.webentity.Rating>> getPaginatedRatings(@RequestHeader HttpHeaders headers,
                                                                                                      @RequestParam(required = false) Integer page,
                                                                                                      @RequestParam(required = false) Integer limit,
                                                                                                      @RequestParam(required = false) Integer userId,
                                                                                                      @RequestParam(required = false) Integer ratingUserId)
        throws Exception{
        String token = headers.get("Authorization").get(0);
        try {
            PaginatedEntity<com.rideshare.rating.webentity.Rating> ratings = ratingService.getAllRatings(token, page, limit, userId, ratingUserId);
            return ResponseEntity.ok(ratings);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/{ratingId}")
    public ResponseEntity<com.rideshare.rating.webentity.Rating> getRatingById(@RequestHeader HttpHeaders headers,
                                                                               @PathVariable Integer ratingId)
            throws Exception {
        try{
            String token = headers.get("Authorization").get(0);
            com.rideshare.rating.webentity.Rating rating = ratingService.getRatingById(ratingId, token);
            return ResponseEntity.ok(rating);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<com.rideshare.rating.webentity.Rating> createRating(@RequestHeader HttpHeaders headers, @RequestBody Rating rating, @AuthenticationPrincipal UserPrincipal userDetails) throws Exception {
        try {
            if(rating.getUserId() != null &&
                    rating.getUserId() == Integer.parseInt(userDetails.getId())){
                throw new Exception("Cannot provide rating to Self");
            }

            String token = headers.get("Authorization").get(0);
            // Check if User exists with user_id
            String requestURL = userinfoUrl + "/users/" +rating.getUserId();
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", token);
            rating.setRatingUserid(Integer.parseInt(userDetails.getId()));
            HttpEntity request = new HttpEntity(header);
            try {
                ResponseEntity<String> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, String.class);
            } catch (Exception e){
                e.printStackTrace();
                throw new Exception("User Does not exist");
            }

            com.rideshare.rating.webentity.Rating newRating = ratingService.create(rating, token);
            return ResponseEntity.ok(newRating);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DeleteSuccess> deleteRide(@PathVariable Integer id) throws Exception {
        try {
            boolean deleted = ratingService.delete(id);
            return ResponseEntity.ok(new DeleteSuccess(deleted));
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
