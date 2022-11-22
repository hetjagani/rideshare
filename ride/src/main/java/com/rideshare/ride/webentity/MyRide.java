package com.rideshare.ride.webentity;

import lombok.Data;

@Data
public class MyRide extends Ride {
    private Boolean isPassenger = false;
}
