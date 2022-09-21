package com.rideshare.payment.service;

import com.rideshare.payment.model.CustomerData;

public interface ICustomerDataService {
    boolean userExists(Integer userId) throws Exception;
    CustomerData getByUserId(Integer userId) throws Exception;
    CustomerData getByStripeCustomerId(String customerId) throws Exception;
    CustomerData create(CustomerData customerData) throws Exception;
    boolean delete(Integer userId) throws Exception;
}
