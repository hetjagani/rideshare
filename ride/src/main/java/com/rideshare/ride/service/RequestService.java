package com.rideshare.ride.service;

import com.rideshare.ride.mapper.RequestMapper;
import com.rideshare.ride.model.RequestStatus;
import com.rideshare.ride.model.UserInfo;
import com.rideshare.ride.util.Pagination;
import com.rideshare.ride.webentity.PaginatedEntity;
import com.rideshare.ride.webentity.Request;
import com.rideshare.ride.webentity.Ride;
import facade.UserInfoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestService implements IRequestService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IRideService rideService;

    @Autowired
    private UserInfoFacade userInfoFacade;

    private final String userRequestsQuery = "SELECT * FROM ride.request WHERE user_id = ? LIMIT ? OFFSET ?;";

    private final String userRequestByIdQuery = "SELECT * FROM ride.request WHERE user_id = ? AND id = ?;";

    private final String requestByIdQuery = "SELECT * FROM ride.request WHERE id = ?;";

    private final String allRequestsQuery = "SELECT * FROM ride.request;";

    private final String insertQuery = "INSERT INTO ride.request(user_id, ride_id, notes, status, created_at) VALUES(?,?,?,?,?) RETURNING id;";

    private final String updateQuery = "UPDATE ride.request\n" +
            "SET stripe_payment_id = ?, receipt_url = ?, status = ?\n" +
            "WHERE id = ?;";

    private final String deleteQuery = "DELETE FROM ride.request WHERE id = ? AND user_id = ?";

    private final String searchQueryT = "SELECT * FROM ride.request WHERE ";

    private final String getCompletedRequestsForRideQuery = "SELECT request.* FROM ride.ride, ride.request\n" +
            "WHERE ride.id = request.ride_id AND request.status = 'COMPLETED' AND ride_id = ?;";

    private final String getCompletedRequestsQuery = "SELECT request.* FROM ride.ride, ride.request\n" +
            "WHERE ride.id = request.ride_id AND request.status = 'COMPLETED';";

    private Map<Integer, Ride> getRideIdMap(String token) throws Exception {
        List<Ride> allRides = rideService.getAll(token);
        Map<Integer, Ride> rideIdMap = new HashMap<>();
        allRides.stream().forEach((ride) -> {
            if(!rideIdMap.containsKey(ride.getId())) {
                rideIdMap.put(ride.getId(), ride);
            }
        });

        return rideIdMap;
    }


    @Override
    public PaginatedEntity<Request> getPaginated(String token, Integer userId, Integer page, Integer limit) throws Exception {

        Integer offset = Pagination.getOffset(page, limit);

        List<Request> userRequests = jdbcTemplate.query(userRequestsQuery, new RequestMapper(), userId, limit, offset);

        Map<Integer, Ride> rideIdMap = getRideIdMap(token);

        List<Request> requestsWithRide = userRequests.stream().map((request -> {
            Ride ride = rideIdMap.get(request.getRideId());
            request.setRide(ride);
            return request;
        })).collect(Collectors.toList());

        return new PaginatedEntity<Request>(requestsWithRide, page, requestsWithRide.size());
    }

    @Override
    public List<Request> getAll(String token) throws Exception {
        List<Request> userRequests = jdbcTemplate.query(allRequestsQuery, new RequestMapper());
        Map<Integer, Ride> rideIdMap = getRideIdMap(token);

        List<Request> requestsWithRide = userRequests.stream().map((request -> {
            Ride ride = rideIdMap.get(request.getRideId());
            request.setRide(ride);
            return request;
        })).collect(Collectors.toList());

        return requestsWithRide;
    }

    @Override
    public PaginatedEntity<Request> searchRequests(String token, Integer userId, String stripePaymentId, String status, Integer rideId, Integer page, Integer limit) throws Exception {

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

        Map<Integer, Ride> rideIdMap = getRideIdMap(token);

        List<Request> requestsWithRide = userRequests.stream().map((request -> {
            Ride ride = rideIdMap.get(request.getRideId());
            request.setRide(ride);
            return request;
        })).collect(Collectors.toList());

        return new PaginatedEntity<Request>(requestsWithRide, page, requestsWithRide.size());
    }

    @Override
    public Request getById(Integer id) {
        return jdbcTemplate.queryForObject(requestByIdQuery, new RequestMapper(), id);
    }

    @Override
    public Request getById(String token, Integer userId, Integer id) throws Exception {
        Request userRequest = jdbcTemplate.queryForObject(userRequestByIdQuery, new RequestMapper(), userId, id);
        Ride ride = rideService.getById(token, userRequest.getRideId());
        userRequest.setRide(ride);
        return userRequest;
    }

    @Override
    public Request create(String token, com.rideshare.ride.model.Request request) throws Exception {
        Integer id = jdbcTemplate.queryForObject(insertQuery, Integer.class, request.getUserId(), request.getRideId(), request.getNotes(), RequestStatus.CREATED, Timestamp.from(Instant.now()));

        Request createdRequest = getById(token, request.getUserId(), id);
        return createdRequest;
    }

    @Override
    public Request update(Request request) throws Exception {
        Request existingRequest = getById(request.getId());

        String stripePaymentId = request.getStripePaymentId() != null ? request.getStripePaymentId() : existingRequest.getStripePaymentId();
        String receiptUrl = request.getReceiptUrl() != null ? request.getReceiptUrl() : existingRequest.getReceiptUrl();
        String status = request.getStatus() != null ? request.getStatus().toUpperCase() : existingRequest.getStatus();

        jdbcTemplate.update(updateQuery, stripePaymentId, receiptUrl, status, request.getId());

        Request updatedRequest = getById(request.getId());

        return updatedRequest;
    }

    @Override
    public boolean delete(Integer requestId, Integer userId) throws Exception {
        Integer affectedRows = jdbcTemplate.update(deleteQuery, requestId, userId);
        return affectedRows != 0;
    }

    @Override
    public List<Request> getCompletedRequestsForRide(Integer rideId) throws Exception {
        return jdbcTemplate.query(getCompletedRequestsForRideQuery, new RequestMapper(), rideId);
    }

    @Override
    public Map<Integer, List<Request>> getCompletedRequests(String token) throws Exception {
        Map<Integer, UserInfo> userMap = userInfoFacade.getAllUsers(token);

        List<Request> allCompletedRequest = jdbcTemplate.query(getCompletedRequestsQuery, new RequestMapper());

        for(Request r: allCompletedRequest) {
            r.setUser(userMap.get(r.getUserId()));
        }

        Map<Integer, List<Request>> requestMap = new HashMap<>();

        for(Request r: allCompletedRequest) {
            if(!requestMap.containsKey(r.getRideId())) {
                List<Request> list = new ArrayList<>();
                list.add(r);
                requestMap.put(r.getRideId(), list);
            } else {
                List<Request> rl = requestMap.get(r.getRideId());
                rl.add(r);
                requestMap.put(r.getRideId(), rl);
            }
        }

        return requestMap;
    }
}
