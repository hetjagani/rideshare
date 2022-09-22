package com.rideshare.payment.webentity;

import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Payment {
    private Integer id;
    private Integer requestId;
    private Request request;
    private Integer userId;
    private String stripeCustomerId;
    private StripeCustomer stripeCustomer;
    private String stripePaymentId;
    private StripePaymentIntent stripePaymentIntent;
    private String status;
}
