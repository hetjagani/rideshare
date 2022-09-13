package com.rideshare.userinfo.service;

import com.rideshare.userinfo.facade.RideServiceFacade;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import com.rideshare.userinfo.webentity.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideService {

    @Autowired
    private RideServiceFacade rideServiceFacade;

    public PaginatedEntity<Ride> getAllPaginated(String token, Integer page, Integer limit, String userId, Boolean all) throws Exception{
        PaginatedEntity<Ride> rides = rideServiceFacade.getAllRides(token, page, limit, Integer.parseInt(userId), all);
        return rides;
    }
}
