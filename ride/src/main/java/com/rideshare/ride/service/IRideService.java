package com.rideshare.ride.service;

import com.rideshare.ride.model.Ride;
import com.rideshare.ride.webentity.PaginatedEntity;
import com.rideshare.ride.webentity.RideRating;

import java.util.List;

public interface IRideService {
    PaginatedEntity<com.rideshare.ride.webentity.Ride> getPaginated(String token, Integer page, Integer limit) throws Exception;
    PaginatedEntity<com.rideshare.ride.webentity.Ride> searchRides(String token, Integer userId, Integer page, Integer limit) throws Exception;
    List<com.rideshare.ride.webentity.Ride> getAll(String token) throws Exception;
    com.rideshare.ride.webentity.Ride getById(String token, Integer id) throws Exception;
    com.rideshare.ride.webentity.Ride create(String token, Ride ride) throws Exception;
    com.rideshare.ride.webentity.Ride updateCapacity(String token, Integer rideId, Integer capacity) throws Exception;
    com.rideshare.ride.webentity.Ride startRide(String token, Integer rideId, Integer userId) throws Exception;
    com.rideshare.ride.webentity.Ride stopRide(String token, Integer rideId, Integer userId) throws Exception;
    boolean delete(Integer id) throws Exception;

    List<RideRating> checkRideForUserIfRated(Integer id) throws Exception;

    Integer createRideRatingForUser(Integer rideId, Integer userId, Integer rId) throws Exception;

    Integer getNoOfRides(Integer userId) throws Exception;
}
