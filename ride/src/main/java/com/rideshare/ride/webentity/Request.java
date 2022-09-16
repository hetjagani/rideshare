package com.rideshare.ride.webentity;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

@Data
public class Request {
    private Integer id;
    private Integer userId;
    private Integer rideId;
    private Ride ride;
    private String stripePaymentId;
    private String receiptUrl;
    private String notes;
    private String status;
    private Timestamp createdAt;
}
