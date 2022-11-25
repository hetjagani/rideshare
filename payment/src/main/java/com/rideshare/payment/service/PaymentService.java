package com.rideshare.payment.service;

import com.rideshare.payment.mapper.PaymentMapper;
import com.rideshare.payment.model.Payment;
import com.rideshare.payment.model.PaymentStatus;
import com.rideshare.payment.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String getAllPaginatedQuery = "SELECT * FROM payment.payment WHERE user_id = ? LIMIT ? OFFSET ?;";
    private final String getByIdQuery = "SELECT * FROM payment.payment WHERE id = ?;";
    private final String getByStripeIdQuery = "SELECT * FROM payment.payment WHERE stripe_payment_id = ?;";
    private final String createQuery = "INSERT INTO payment.payment(request_id, user_id, stripe_customer_id, stripe_payment_id, status) VALUES(?,?,?,?,?) RETURNING id";
    private final String updateStatusQuery = "UPDATE payment.payment SET status = ? WHERE id = ?";
    private final String deleteQuery = "DELETE FROM payment.payment WHERE id = ?";

    @Override
    public List<Payment> getAllPaginated(Integer userId, Integer page, Integer limit) throws Exception {
        Integer offset = Pagination.getOffset(page, limit);
        return jdbcTemplate.query(getAllPaginatedQuery, new PaymentMapper(), userId, limit, offset);
    }

    @Override
    public Payment getById(Integer id) throws Exception {
        return jdbcTemplate.queryForObject(getByIdQuery, new PaymentMapper(), id);
    }

    @Override
    public Payment getByStripePaymentId(String id) throws Exception {
        return jdbcTemplate.queryForObject(getByStripeIdQuery, new PaymentMapper(), id);
    }

    @Override
    public Payment create(Payment payment) throws Exception {
        Integer createdId = jdbcTemplate.queryForObject(createQuery, Integer.class, payment.getRequestId(), payment.getUserId(), payment.getStripeCustomerId(), payment.getStripePaymentId(), PaymentStatus.CREATED);
        Payment createdPayment = getById(createdId);
        return createdPayment;
    }

    @Override
    public Payment updateStatus(Integer id, String status) throws Exception {
        jdbcTemplate.update(updateStatusQuery, status, id);
        Payment updatedPayment = getById(id);
        return updatedPayment;
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        Integer affectedRows = jdbcTemplate.update(deleteQuery, id);
        return affectedRows != 0;
    }
}
