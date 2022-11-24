package com.rideshare.ride.mapper;


import com.rideshare.ride.model.Address;
import com.rideshare.ride.webentity.Ride;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RideWithAddressMapper implements RowMapper<Ride> {
    @Override
    public Ride mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address startAddress = new Address(rs.getInt(14), rs.getString(15), rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20), rs.getFloat(21), rs.getFloat(22));

        Address endAddress = new Address(rs.getInt(23), rs.getString(24), rs.getString(25), rs.getString(26), rs.getString(27), rs.getString(28), rs.getString(29), rs.getFloat(30), rs.getFloat(31));

        Ride ride = new Ride();
        ride.setId(rs.getInt(1));
        ride.setStartAddress(startAddress);
        ride.setEndAddress(endAddress);
        ride.setStatus(rs.getString("status"));
        ride.setPostId(rs.getInt("post_id"));
        ride.setUserId(rs.getInt("user_id"));
        ride.setNoPassengers(rs.getInt("no_passengers"));
        ride.setCapacity(rs.getInt("capacity"));
        ride.setPricePerPerson(rs.getFloat("price_per_person"));
        ride.setCreatedAt(rs.getTimestamp("created_at"));
        ride.setStartedAt(rs.getTimestamp("started_at"));
        ride.setEndedAt(rs.getTimestamp("ended_at"));
        ride.setRideTime(rs.getTimestamp("ride_time"));
        return ride;
    }
}
