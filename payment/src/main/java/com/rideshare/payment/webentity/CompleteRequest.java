package com.rideshare.payment.webentity;

import lombok.Data;

@Data
public class CompleteRequest {
    private String stripePaymentId;
    private String receiptUrl;
}
