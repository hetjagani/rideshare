package com.rideshare.ride.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RideTag {
    private Integer rideId;
    private Integer tagId;
    private Tag tag;
}
