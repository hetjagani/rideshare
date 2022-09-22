package com.rideshare.payment.webentity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripePaymentIntent {
    private String id;
    private Long amount;
    private String currency;
}
