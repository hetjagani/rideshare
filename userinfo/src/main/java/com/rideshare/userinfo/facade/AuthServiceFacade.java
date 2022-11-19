package com.rideshare.userinfo.facade;

import com.rideshare.userinfo.model.User;
import com.rideshare.userinfo.webentity.UpdateRoleRequest;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class AuthServiceFacade {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.auth.url}")
    private String authUrl;

    public User updateRoles(List<String> roles, String token) throws Exception{
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", token);

        HttpEntity request = new HttpEntity(new UpdateRoleRequest(roles), header);
        String url = String.format("%s/%s", authUrl, "/auth/roles");

        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.PUT, request, User.class);

        return response.getBody();
    }
}
