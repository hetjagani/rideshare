package com.rideshare.payment.mapper;

import com.rideshare.payment.model.CustomerData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDataMapper implements RowMapper<CustomerData> {
    @Override
    public CustomerData mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CustomerData(rs.getInt("user_id"), rs.getString("stripe_customer_id"));
    }
}
