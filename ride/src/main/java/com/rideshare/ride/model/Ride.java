package com.rideshare.ride.model;

import lombok.Data;

import java.util.List;

@Data
public class Ride {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private Float pricePerPerson;
    private Integer noPassengers;
    private String status;
    private List<Integer> tagIds;
    private Integer startAddress;
    private Integer endAddress;
}
