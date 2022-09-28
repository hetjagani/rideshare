package com.rideshare.userinfo.service;

import com.rideshare.userinfo.model.User;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import com.rideshare.userinfo.webentity.Rating;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RatingService {

    private final Logger logger = LogManager.getLogger(UserInfoService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.rating.url}")
    private String ratingURL;

    public PaginatedEntity<Rating> getAllPaginated(String token, Integer page, Integer limit, Integer ratingUserId, Boolean all) throws Exception {
        String requestURL = ratingURL + "/ratings" + "?page={page}&limit={limit}&ratingUserId={ratingUserId}&all={all}";
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", page);
        queryParams.put("limit", limit);
        queryParams.put("ratingUserId", ratingUserId);
        queryParams.put("all", all);

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", token);

        HttpEntity request = new HttpEntity(header);
        ResponseEntity<PaginatedEntity<Rating>> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET,
                                    request, new ParameterizedTypeReference<PaginatedEntity<Rating>>() {}, queryParams);

        return responseEntity.getBody();
    }
}
