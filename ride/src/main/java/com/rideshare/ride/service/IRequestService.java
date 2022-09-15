package com.rideshare.ride.service;

import com.rideshare.ride.webentity.PaginatedEntity;
import com.rideshare.ride.webentity.Request;

import java.util.List;

public interface IRequestService {
    PaginatedEntity<Request> getPaginated(Integer userId, Integer page, Integer limit) throws Exception;
    List<Request> getAll() throws Exception;
    PaginatedEntity<Request> searchRequests(Integer userId, String stripePaymentId, String status, Integer rideId, Integer page, Integer limit) throws Exception;
    Request getById(Integer userId, Integer id) throws Exception;
    Request create(com.rideshare.ride.model.Request request) throws Exception;
    Request update(com.rideshare.ride.model.Request request) throws Exception;
    boolean delete(Integer requestId, Integer userId) throws Exception;
}
