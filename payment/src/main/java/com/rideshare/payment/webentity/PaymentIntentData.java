package com.rideshare.payment.webentity;

import com.rideshare.payment.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentIntentData {
    private String paymentIntent;
    private String ephemeralKey;
    private String customer;
    private String publishableKey;
    private Payment payment;

    public PaymentIntentData() {}
}
