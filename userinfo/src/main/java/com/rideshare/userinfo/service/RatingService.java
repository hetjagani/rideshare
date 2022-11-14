package com.rideshare.userinfo.service;

import com.rideshare.userinfo.model.User;
import com.rideshare.userinfo.webentity.DeleteSuccess;
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

    public Float getAvgRating(Integer userId, String token) throws Exception{
        try{
            String requestURL = ratingURL + "/ratings/avgRating/"+userId;

            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", token);

            HttpEntity request = new HttpEntity(header);
            ResponseEntity<Float> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET,
                    request, Float.class);
            return responseEntity.getBody();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
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

    public Rating create(String token, com.rideshare.userinfo.model.Rating rating) throws Exception{
        try {
            String requestURL = ratingURL + "/ratings";
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", token);

            HttpEntity<com.rideshare.userinfo.model.Rating> entity
                    = new HttpEntity<com.rideshare.userinfo.model.Rating>(rating,header);

            ResponseEntity<Rating> responseEntity = restTemplate.exchange(requestURL, HttpMethod.POST, entity, Rating.class);

            return responseEntity.getBody();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Boolean delete(String token, Integer id) throws Exception{
        try {
            String requestURL = ratingURL + "/ratings/"+ id;
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", token);

            HttpEntity request = new HttpEntity(header);

            ResponseEntity<DeleteSuccess> responseEntity = restTemplate.exchange(requestURL, HttpMethod.DELETE, request, DeleteSuccess.class);
            return responseEntity.getBody().getSuccess();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
