package com.rideshare.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Payment {
    private Integer id;
    private Integer requestId;
    private Integer userId;
    private String stripeCustomerId;
    private String stripePaymentId;
    private String status;

    public Payment() {}
}
