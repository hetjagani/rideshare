package com.rideshare.chat.facade;


import com.rideshare.chat.webentity.PaginatedEntity;
import com.rideshare.chat.webentity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;
import java.util.List;

public class UserInfoFacade {
    @Value("${app.userinfo.url}")
    private String userInfoUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<UserInfo> getAllUsers(final String token) throws Exception{
        try{
            final String requestUrl = userInfoUrl + "/users";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization",token);

            HttpEntity request = new HttpEntity(headers);

            ResponseEntity<PaginatedEntity<UserInfo>> listUsers = restTemplate.exchange(requestUrl, HttpMethod.GET, request, new ParameterizedTypeReference<PaginatedEntity<UserInfo>>() {});

            return listUsers.getBody().getNodes();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
