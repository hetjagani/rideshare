package com.rideshare.ride.service;

import com.rideshare.ride.webentity.PaginatedEntity;
import com.rideshare.ride.webentity.Request;

import java.util.List;

public interface IRequestService {
    PaginatedEntity<Request> getPaginated(String token, Integer userId, Integer page, Integer limit) throws Exception;
    List<Request> getAll(String token) throws Exception;
    PaginatedEntity<Request> searchRequests(String token, Integer userId, String stripePaymentId, String status, Integer rideId, Integer page, Integer limit) throws Exception;
    Request getById(String token, Integer userId, Integer id) throws Exception;
    Request create(String token, com.rideshare.ride.model.Request request) throws Exception;
    Request update(String token, com.rideshare.ride.model.Request request) throws Exception;
    boolean delete(Integer requestId, Integer userId) throws Exception;
}
