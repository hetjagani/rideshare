package com.rideshare.ride.webentity;

import lombok.Data;

import java.util.List;

@Data
public class MyRide extends Ride {
    private Boolean isPassenger = false;
    private Boolean isRatedByUser = false;
    private Integer ratingsId = -1;
    private List<Request> requests;
}
