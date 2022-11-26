package com.rideshare.ride.webentity;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;


public class RideRating {
    private Integer rideId;
    private Integer ratingId;

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public RideRating(Integer rideId, Integer ratingId) {
        this.rideId = rideId;
        this.ratingId = ratingId;
    }
}
