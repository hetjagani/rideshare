package com.rideshare.post.facade;

import com.rideshare.post.model.UserInfo;
import com.rideshare.post.webentity.PaginatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoFacade {

    @Value("${app.userinfo.url}")
    private String userInfoUrl;

    @Autowired
    RestTemplate restTemplate;


    public Map<Integer, UserInfo> getAllUsers(String token) throws Exception {
        String requestUrl = String.format("%s/%s?all=true", userInfoUrl, "/users");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<PaginatedEntity<UserInfo>> response = restTemplate.exchange(requestUrl, HttpMethod.GET, request, new ParameterizedTypeReference<PaginatedEntity<UserInfo>>() {});

        List<UserInfo> userInfos = response.getBody().getNodes();

        Map<Integer, UserInfo> result = new HashMap<>();

        for(UserInfo ui : userInfos) {
            result.put(ui.getId(), ui);
        }

        return result;
    }

}
