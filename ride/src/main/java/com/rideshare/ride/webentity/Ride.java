package com.rideshare.ride.webentity;

import com.rideshare.ride.model.Address;
import com.rideshare.ride.model.Tag;
import lombok.Data;

import java.util.List;

@Data
public class Ride {
    private Integer id;
    private Integer postId;
    private Float pricePerPerson;
    private Integer noPassengers;
    private String status;
    private List<Tag> tags;
    private Address startAddress;
    private Address endAddress;
}
