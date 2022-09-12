package com.rideshare.ride.mapper;


import com.rideshare.ride.model.Address;
import com.rideshare.ride.webentity.Ride;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RideWithAddressMapper implements RowMapper<Ride> {
    @Override
    public Ride mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address startAddress = new Address(rs.getInt(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getFloat(16), rs.getFloat(17));

        Address endAddress = new Address(rs.getInt(18), rs.getString(19), rs.getString(20), rs.getString(21), rs.getString(22), rs.getString(23), rs.getString(24), rs.getFloat(25), rs.getFloat(26));

        Ride ride = new Ride();
        ride.setId(rs.getInt(1));
        ride.setStartAddress(startAddress);
        ride.setEndAddress(endAddress);
        ride.setStatus(rs.getString("status"));
        ride.setPostId(rs.getInt("post_id"));
        ride.setUserId(rs.getInt("user_id"));
        ride.setNoPassengers(rs.getInt("no_passengers"));
        ride.setPricePerPerson(rs.getFloat("price_per_person"));

        return ride;
    }
}
