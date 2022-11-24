package com.rideshare.post.webentity;

import com.rideshare.post.model.Address;
import com.rideshare.post.model.Tag;
import com.rideshare.post.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

@Data
@AllArgsConstructor
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
    private Timestamp createdAt;
    private Timestamp startedAt;
    private Timestamp endedAt;
    private Timestamp rideTime;
    private Duration duration;
    private UserInfo user;

    public Ride(){}
}
