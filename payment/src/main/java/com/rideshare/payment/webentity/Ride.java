package com.rideshare.payment.webentity;

import lombok.Data;

import java.util.List;

@Data
public class Ride {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private Float pricePerPerson;
    private Integer noPassengers;
    private Integer capacity;
    private String status;
    private List<Tag> tags;
    private Address startAddress;
    private Address endAddress;
}
