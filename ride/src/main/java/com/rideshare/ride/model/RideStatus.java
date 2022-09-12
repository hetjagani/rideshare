package com.rideshare.ride.model;

import lombok.Data;

import javax.naming.OperationNotSupportedException;

@Data
public class RideStatus {
    public static final String CREATED = "CREATED";
    public static final String ACTIVE = "ACTIVE";
    public static final String COMPLETED = "COMPLETED";
    public static final String DELETED = "DELETED";

    public RideStatus() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Cannot create object of this type.");
    }
}
