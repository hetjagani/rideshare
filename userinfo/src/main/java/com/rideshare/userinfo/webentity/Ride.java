package com.rideshare.userinfo.webentity;

import com.rideshare.userinfo.model.Tag;
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
    private List<Tag> tags;
    private Address startAddress;
    private Address endAddress;
}
