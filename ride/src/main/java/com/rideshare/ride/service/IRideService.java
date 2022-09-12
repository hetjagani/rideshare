package com.rideshare.ride.service;

import com.rideshare.ride.model.Ride;
import com.rideshare.ride.webentity.PaginatedEntity;

import java.util.List;

public interface IRideService {
    PaginatedEntity<com.rideshare.ride.webentity.Ride> getPaginated(Integer page, Integer limit) throws Exception;
    PaginatedEntity<com.rideshare.ride.webentity.Ride> searchRides(Integer userId, Integer page, Integer limit) throws Exception;
    List<com.rideshare.ride.webentity.Ride> getAll() throws Exception;
    com.rideshare.ride.webentity.Ride getById(Integer id) throws Exception;
    com.rideshare.ride.webentity.Ride create(Ride ride) throws Exception;
    boolean delete(Integer id) throws Exception;
}
