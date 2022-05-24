package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.exception.ForbiddenException;
import com.rideshare.userinfo.model.UserInfo;
import com.rideshare.userinfo.security.UserPrincipal;
import com.rideshare.userinfo.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUsers(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception {
        try {
            logger.debug(headers.get("Authorization"));
            String token = headers.get("Authorization").get(0);

            List<UserInfo> userInfoList = userInfoService.getAllPaginated(token, page, limit);

            return ResponseEntity.ok(userInfoList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserInfo> getSelfUserInfo(Authentication auth) throws Exception {
        try {
            UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            Integer userID = Integer.parseInt(userDetails.getId());

            UserInfo userInfo = userInfoService.getById(userID);
            userInfo.setEmail(userDetails.getEmail());
            userInfo.setPhoneNo(userDetails.getPhoneNo());
            userInfo.setVerified(userDetails.isEnabled());
            userInfo.setRoles(userDetails.getAuthorities().stream().map((e)->e.toString()).collect(Collectors.toList()));

            return ResponseEntity.ok(userInfo);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<UserInfo> createUserInfo(@RequestBody @Valid com.rideshare.userinfo.webentity.UserInfo userInfo, Authentication auth) throws Exception {
        try {
            UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            // check if logged in user is same as provided user
            if(Integer.parseInt(userDetails.getId()) != userInfo.getId()) {
                throw new ForbiddenException("cannot add userinfo of other user then self");
            }
            // if user exist then save user info in the db
            UserInfo toCreateUserInfo = new UserInfo();
            toCreateUserInfo.setId(userInfo.getId());
            toCreateUserInfo.setEmail(userDetails.getEmail());
            toCreateUserInfo.setPhoneNo(userDetails.getPhoneNo());
            toCreateUserInfo.setVerified(userDetails.isEnabled());
            toCreateUserInfo.setRoles(userDetails.getAuthorities().stream().map((e)->e.toString()).collect(Collectors.toList()));
            toCreateUserInfo.setFirstName(userInfo.getFirstName());
            toCreateUserInfo.setLastName(userInfo.getLastName());
            toCreateUserInfo.setProfileImage(userInfo.getProfileImage());

            UserInfo result = userInfoService.create(toCreateUserInfo);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
