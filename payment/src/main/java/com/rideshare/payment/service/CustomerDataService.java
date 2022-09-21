package com.rideshare.payment.service;

import com.rideshare.payment.mapper.CustomerDataMapper;
import com.rideshare.payment.model.CustomerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerDataService implements ICustomerDataService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String getByUserIdQuery = "SELECT * FROM payment.\"customerData\" WHERE user_id = ?";
    private final String getByStripeCustomerIdQuery = "SELECT * FROM payment.\"customerData\" WHERE stripe_customer_id = ?";
    private final String createQuery = "INSERT INTO payment.\"customerData\"(user_id, stripe_customer_id) VALUES(?,?) RETURNING user_id";
    private final String deleteQuery = "DELETE FROM payment.\"customerData\" WHERE user_id = ?";

    @Override
    public boolean userExists(Integer userId) throws Exception {
        try {
            CustomerData customerData = jdbcTemplate.queryForObject(getByUserIdQuery, new CustomerDataMapper(), userId);
            if (customerData == null) {
                return false;
            }
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public CustomerData getByUserId(Integer userId) throws Exception {
        return jdbcTemplate.queryForObject(getByUserIdQuery, new CustomerDataMapper(), userId);
    }

    @Override
    public CustomerData getByStripeCustomerId(String customerId) throws Exception {
        return jdbcTemplate.queryForObject(getByStripeCustomerIdQuery, new CustomerDataMapper(), customerId);
    }

    @Override
    public CustomerData create(CustomerData customerData) throws Exception {
        Integer userId = jdbcTemplate.queryForObject(createQuery, Integer.class, customerData.getUserId(), customerData.getStripeCustomerId());
        CustomerData createdData = getByUserId(userId);
        return createdData;
    }

    @Override
    public boolean delete(Integer userId) throws Exception {
        Integer affectedRows = jdbcTemplate.update(deleteQuery, userId);
        return affectedRows != 0;
    }
}
