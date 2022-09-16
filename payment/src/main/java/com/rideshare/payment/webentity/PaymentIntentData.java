package com.rideshare.payment.webentity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentIntentData {
    private String paymentIntent;
    private String ephemeralKey;
    private String customer;
    private String publishableKey;

    public PaymentIntentData() {}
}
