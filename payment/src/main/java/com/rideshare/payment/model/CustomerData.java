package com.rideshare.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerData {
    private Integer userId;
    private String stripeCustomerId;

    public CustomerData() {}
}
