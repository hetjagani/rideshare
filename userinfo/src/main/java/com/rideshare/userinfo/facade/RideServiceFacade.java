package com.rideshare.userinfo.facade;

import com.rideshare.userinfo.webentity.PaginatedEntity;
import com.rideshare.userinfo.webentity.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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

    public PaginatedEntity<Ride> getAllRides(String token, Integer page, Integer limit, Integer userId,
                                                             Boolean all) {

        // all = true only when user is Admin
        String requestURL = rideUrl + "/rides" + "?all=" + all + (all != true ? "&userId=" +userId : "&page="+(page != null ? page : "")
                                +"&limit="+(limit != null ? limit : ""));

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", token);

        HttpEntity request = new HttpEntity(header);

        try {
            ResponseEntity<PaginatedEntity<Ride>> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, new ParameterizedTypeReference<PaginatedEntity<Ride>>() {});
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
