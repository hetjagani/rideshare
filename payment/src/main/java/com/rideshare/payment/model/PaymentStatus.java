package com.rideshare.payment.model;

import javax.naming.OperationNotSupportedException;

public class PaymentStatus {
    public static final String CREATED = "CREATED";
    public static final String COMPLETED = "COMPLETED";

    public PaymentStatus() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Cannot create object of this type.");
    }
}
