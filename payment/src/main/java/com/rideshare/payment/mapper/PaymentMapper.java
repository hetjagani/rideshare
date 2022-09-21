package com.rideshare.payment.mapper;

import com.rideshare.payment.model.Payment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentMapper implements RowMapper<Payment> {
    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Payment(rs.getInt("id"), rs.getInt("request_id"), rs.getInt("user_id"), rs.getString("stripe_customer_id"), rs.getString("stripe_payment_id"), rs.getString("status"));
    }
}
