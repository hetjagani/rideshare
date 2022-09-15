package com.rideshare.ride.model;

import javax.naming.OperationNotSupportedException;

public class RequestStatus {
    public static final String CREATED = "CREATED";
    public static final String COMPLETED = "COMPLETED";

    public RequestStatus() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Cannot create object of this type.");
    }
}
