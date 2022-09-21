package com.rideshare.payment.service;

import com.rideshare.payment.model.Payment;

import java.util.List;

public interface IPaymentService {
    List<Payment> getAllPaginated(Integer userId, Integer page, Integer limit) throws Exception;
    Payment getById(Integer id) throws Exception;
    Payment create(Payment payment) throws Exception;
    Payment updateStatus(Integer id, String status) throws Exception;
    boolean delete(Integer id) throws Exception;
}
