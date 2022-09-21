package com.rideshare.payment.facade;

import com.rideshare.payment.webentity.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


public class RideServiceFacade {

    @Value("${app.ride.url}")
    private String rideUrl;

    @Autowired
    private RestTemplate restTemplate;

    public Request getRequest(Integer requestId, String token) throws Exception {
        String urlEndpoint = String.format("%s/%s/%s", rideUrl, "/rides/requests", requestId);

         HttpHeaders headers = new HttpHeaders();
         headers.add("Authorization", token);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Request> response = restTemplate.exchange(urlEndpoint, HttpMethod.GET, request, Request.class);

        return response.getBody();
    }
}
