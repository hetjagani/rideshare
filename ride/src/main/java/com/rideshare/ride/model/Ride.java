package com.rideshare.ride.model;

import lombok.Data;

import java.util.List;

@Data
public class Ride {
    private String id;
    private String postId;
    private Float pricePerPerson;
    private Integer noPassengers;
    private String status;
    private List<Integer> tagIds;
    private Integer startAddress;
    private Integer endAddress;
}
