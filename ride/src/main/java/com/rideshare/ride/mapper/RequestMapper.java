package com.rideshare.ride.mapper;

import com.rideshare.ride.webentity.Request;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;


public class RequestMapper implements RowMapper<Request> {
    @Override
    public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
        Request request = new Request();
        request.setId(rs.getInt("id"));
        request.setRideId(rs.getInt("ride_id"));
        request.setStatus(rs.getString("status"));
        request.setNotes(rs.getString("notes"));
        request.setUserId(rs.getInt("user_id"));
        request.setCreatedAt(rs.getTimestamp("created_at"));
        request.setReceiptUrl(rs.getString("receipt_url"));
        request.setStripePaymentId(rs.getString("stripe_payment_id"));
        return request;
    }
}
