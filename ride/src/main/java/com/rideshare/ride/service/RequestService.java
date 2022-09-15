package com.rideshare.ride.service;

import com.rideshare.ride.mapper.RequestMapper;
import com.rideshare.ride.model.RequestStatus;
import com.rideshare.ride.util.Pagination;
import com.rideshare.ride.webentity.PaginatedEntity;
import com.rideshare.ride.webentity.Request;
import com.rideshare.ride.webentity.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequestService implements IRequestService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IRideService rideService;

    private final String userRequestsQuery = "SELECT * FROM ride.request WHERE user_id = ? LIMIT ? OFFSET ?;";

    private final String userRequestByIdQuery = "SELECT * FROM ride.request WHERE user_id = ? AND id = ?;";

    private final String allRequestsQuery = "SELECT * FROM ride.request;";

    private final String insertQuery = "INSERT INTO ride.request(user_id, ride_id, notes, status, created_at) VALUES(?,?,?,?,?) RETURNING id;";

    private final String updateQuery = "UPDATE ride.request\n" +
            "SET stripe_payment_id = ?, receipt_url = ?, status = ?\n" +
            "WHERE id = ?;";

    private final String deleteQuery = "DELETE FROM ride.request WHERE id = ? AND user_id = ?";

    private final String searchQueryT = "SELECT * FROM ride.request WHERE ";


    private Map<Integer, Ride> getRideIdMap() throws Exception {
        List<Ride> allRides = rideService.getAll();
        Map<Integer, Ride> rideIdMap = new HashMap<>();
        allRides.stream().forEach((ride) -> {
            if(!rideIdMap.containsKey(ride.getId())) {
                rideIdMap.put(ride.getId(), ride);
            }
        });

        return rideIdMap;
    }


    @Override
    public PaginatedEntity<Request> getPaginated(Integer userId, Integer page, Integer limit) throws Exception {

        Integer offset = Pagination.getOffset(page, limit);

        List<Request> userRequests = jdbcTemplate.query(userRequestsQuery, new RequestMapper(), userId, limit, offset);

        Map<Integer, Ride> rideIdMap = getRideIdMap();

        List<Request> requestsWithRide = userRequests.stream().map((request -> {
            Ride ride = rideIdMap.get(request.getRideId());
            request.setRide(ride);
            return request;
        })).collect(Collectors.toList());

        return new PaginatedEntity<Request>(requestsWithRide, page, requestsWithRide.size());
    }

    @Override
    public List<Request> getAll() throws Exception {
        List<Request> userRequests = jdbcTemplate.query(allRequestsQuery, new RequestMapper());
        Map<Integer, Ride> rideIdMap = getRideIdMap();

        List<Request> requestsWithRide = userRequests.stream().map((request -> {
            Ride ride = rideIdMap.get(request.getRideId());
            request.setRide(ride);
            return request;
        })).collect(Collectors.toList());

        return requestsWithRide;
    }

    @Override
    public PaginatedEntity<Request> searchRequests(Integer userId, String stripePaymentId, String status, Integer rideId, Integer page, Integer limit) throws Exception {

        StringBuilder sb = new StringBuilder(searchQueryT);

        if(userId != null && userId != 0) {
            sb.append("user_id = "+userId+" AND ");
        }
        if(stripePaymentId != null && !stripePaymentId.isEmpty() && !stripePaymentId.isBlank()) {
            sb.append("stripe_payment_id = '"+stripePaymentId+"' AND ");
        }
        if(status != null && !status.isEmpty() && !status.isBlank()) {
            sb.append("status = '"+status+"' AND ");
        }
        if(rideId != null && rideId != 0) {
            sb.append("ride_id = "+rideId+" AND ");
        }

        String searchQuery;
        if(sb.toString().endsWith(" AND ")) {
            searchQuery = sb.substring(0, sb.length()-5);
        } else {
            searchQuery = sb.toString();
        }
        searchQuery += " LIMIT ? OFFSET ?";

        Integer offset = Pagination.getOffset(page, limit);

        List<Request> userRequests = jdbcTemplate.query(searchQuery, new RequestMapper(), limit, offset);

        Map<Integer, Ride> rideIdMap = getRideIdMap();

        List<Request> requestsWithRide = userRequests.stream().map((request -> {
            Ride ride = rideIdMap.get(request.getRideId());
            request.setRide(ride);
            return request;
        })).collect(Collectors.toList());

        return new PaginatedEntity<Request>(requestsWithRide, page, requestsWithRide.size());
    }

    @Override
    public Request getById(Integer userId, Integer id) throws Exception {
        Request userRequest = jdbcTemplate.queryForObject(userRequestByIdQuery, new RequestMapper(), userId, id);
        Ride ride = rideService.getById(userRequest.getRideId());
        userRequest.setRide(ride);
        return userRequest;
    }

    @Override
    public Request create(com.rideshare.ride.model.Request request) throws Exception {
        Integer id = jdbcTemplate.queryForObject(insertQuery, Integer.class, request.getUserId(), request.getRideId(), request.getNotes(), RequestStatus.CREATED, Timestamp.from(Instant.now()));

        Request createdRequest = getById(request.getUserId(), id);
        return createdRequest;
    }

    @Override
    public Request update(com.rideshare.ride.model.Request request) throws Exception {
        Request existingRequest = getById(request.getUserId(), request.getId());

        String stripePaymentId = request.getStripePaymentId() != null ? request.getStripePaymentId() : existingRequest.getStripePaymentId();
        String receiptUrl = request.getReceiptUrl() != null ? request.getReceiptUrl() : existingRequest.getReceiptUrl();
        String status = request.getStatus() != null ? request.getStatus().toUpperCase() : existingRequest.getStatus();

        jdbcTemplate.update(updateQuery, stripePaymentId, receiptUrl, status, request.getId());

        Request updatedRequest = getById(request.getUserId(), request.getId());

        return updatedRequest;
    }

    @Override
    public boolean delete(Integer requestId, Integer userId) throws Exception {
        Integer affectedRows = jdbcTemplate.update(deleteQuery, requestId, userId);
        return affectedRows != 0;
    }
}
