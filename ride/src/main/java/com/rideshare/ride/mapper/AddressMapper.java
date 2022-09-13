package com.rideshare.ride.mapper;

import com.rideshare.ride.model.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressMapper implements RowMapper<Address> {
    @Override
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Address(rs.getInt("id"),
                rs.getString("street"),
                rs.getString("line"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("country"),
                rs.getString("zipcode"),
                rs.getFloat("lat"),
                rs.getFloat("long") );
    }
}
