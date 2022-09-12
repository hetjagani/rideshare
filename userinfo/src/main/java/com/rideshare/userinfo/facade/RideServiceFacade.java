package com.rideshare.userinfo.facade;

import com.rideshare.userinfo.exception.ForbiddenException;
import com.rideshare.userinfo.exception.UserDoesNotExistException;
import com.rideshare.userinfo.model.User;
import com.rideshare.userinfo.webentity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RideServiceFacade {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.ride.url}")
    private String rideUrl;

    private Logger logger = LogManager.getLogger();

    public Boolean checkIfAddressIsValid(String token, Integer id) throws Exception {
        String requestURL = rideUrl + "/addresses/" + id;
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", token);

        HttpEntity request = new HttpEntity(header);
        try {
            ResponseEntity<String> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, String.class);
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("Address Does not exist");
        }
        return true;
    }
}
