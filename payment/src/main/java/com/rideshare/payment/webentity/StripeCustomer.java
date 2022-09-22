package com.rideshare.payment.webentity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripeCustomer {
    private String id;
    private String email;
    private String phone;
}
