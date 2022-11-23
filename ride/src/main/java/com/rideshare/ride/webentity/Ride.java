package com.rideshare.ride.webentity;

import com.rideshare.ride.model.Address;
import com.rideshare.ride.model.Tag;
import com.rideshare.ride.model.UserInfo;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

@Data
public class Ride implements Comparable<Ride> {
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
    private Duration duration;
    private UserInfo user;

    @Override
    public int compareTo(Ride o) {
        return o.getCreatedAt().compareTo(this.createdAt);
    }
}
