package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.exception.ForbiddenException;
import com.rideshare.userinfo.model.UserInfo;
import com.rideshare.userinfo.security.UserPrincipal;
import com.rideshare.userinfo.service.UserInfoService;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RestTemplate restTemplate;

    private final Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUsers(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);

            List<UserInfo> userInfoList = userInfoService.getAllPaginated(token, page, limit);

            return ResponseEntity.ok(userInfoList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserInfo> getSelfUserInfo(@AuthenticationPrincipal UserPrincipal userDetails) throws Exception {
        try {
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

    @PutMapping
    public ResponseEntity<UserInfo> saveUserInfo(@RequestBody @Valid com.rideshare.userinfo.webentity.UserInfo userInfo, @AuthenticationPrincipal UserPrincipal userDetails) throws Exception {
        // prepare object to store in db
        UserInfo toSaveUserInfo = new UserInfo();
        toSaveUserInfo.setId(userInfo.getId());
        toSaveUserInfo.setEmail(userDetails.getEmail());
        toSaveUserInfo.setPhoneNo(userDetails.getPhoneNo());
        toSaveUserInfo.setVerified(userDetails.isEnabled());
        toSaveUserInfo.setRoles(userDetails.getAuthorities().stream().map((e)->e.toString()).collect(Collectors.toList()));
        toSaveUserInfo.setFirstName(userInfo.getFirstName());
        toSaveUserInfo.setLastName(userInfo.getLastName());
        toSaveUserInfo.setProfileImage(userInfo.getProfileImage());
        try {
            // check if logged in user is same as provided user
            if(Integer.parseInt(userDetails.getId()) != userInfo.getId()) {
                throw new ForbiddenException("cannot add userinfo of other user then self");
            }

            UserInfo presentUser = userInfoService.getById(Integer.parseInt(userDetails.getId()));
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

}
