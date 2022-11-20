package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.model.UserInfo;
import com.rideshare.userinfo.security.UserPrincipal;
import com.rideshare.userinfo.service.IUserInfoService;
import com.rideshare.userinfo.service.RatingService;
import com.rideshare.userinfo.service.RideService;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import com.rideshare.userinfo.webentity.Rating;
import com.rideshare.userinfo.webentity.Ride;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RideService rideService;

    @Autowired
    private RatingService ratingService;

    private final Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<PaginatedEntity<UserInfo>> getAllUsers(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);

            PaginatedEntity<UserInfo> userInfoList = userInfoService.getAllPaginated(token, page, limit);

            return ResponseEntity.ok(userInfoList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserInfo> getSelfUserInfo(@AuthenticationPrincipal UserPrincipal userDetails, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Integer userID = Integer.parseInt(userDetails.getId());

            UserInfo userInfo = userInfoService.getById(userID);
            userInfo.setEmail(userDetails.getEmail());
            userInfo.setPhoneNo(userDetails.getPhoneNo());
            userInfo.setVerified(userDetails.isEnabled());
            userInfo.setRoles(userDetails.getAuthorities().stream().map((e)->e.toString()).collect(Collectors.toList()));

            List<Ride> rides = rideService.getAllPaginated(token, null, null, userDetails.getId(), true).getNodes();
            userInfo.setRides(rides.size());

            List<Rating> ratings = ratingService.getAllPaginated(token, null, null, userID, true).getNodes();
            Float total = 0F;
            for(Rating r : ratings) {
                total += r.getRating();
            }
            if (ratings.size() != 0)
                userInfo.setRating(total/ratings.size());
            else userInfo.setRating(0F);

            return ResponseEntity.ok(userInfo);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping
    public ResponseEntity<UserInfo> saveUserInfo(@RequestBody com.rideshare.userinfo.webentity.UserInfo userInfo, @AuthenticationPrincipal UserPrincipal userDetails) throws Exception {
        Integer userId = Integer.parseInt(userDetails.getId());
        // prepare object to store in db
        UserInfo toSaveUserInfo = new UserInfo();
        toSaveUserInfo.setId(userId);
        toSaveUserInfo.setEmail(userDetails.getEmail());
        toSaveUserInfo.setPhoneNo(userDetails.getPhoneNo());
        toSaveUserInfo.setVerified(userDetails.isEnabled());
        toSaveUserInfo.setRoles(userDetails.getAuthorities().stream().map((e)->e.toString()).collect(Collectors.toList()));
        toSaveUserInfo.setFirstName(userInfo.getFirstName());
        toSaveUserInfo.setLastName(userInfo.getLastName());
        toSaveUserInfo.setProfileImage(userInfo.getProfileImage());
        try {
            UserInfo presentUser = userInfoService.getById(userId);
            UserInfo result = null;
            if(presentUser != null) {
                result = userInfoService.update(toSaveUserInfo);
            }

            return ResponseEntity.ok(result);
        } catch (EmptyResultDataAccessException emptyEx) {
            UserInfo result = userInfoService.create(toSaveUserInfo);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/{userID}")
    public ResponseEntity<UserInfo> getUserInfoById(@PathVariable Integer userID, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            UserInfo userInfo = userInfoService.getById(userID);

            return ResponseEntity.ok(userInfo);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
