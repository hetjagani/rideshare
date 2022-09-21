package com.rideshare.payment.webentity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private Integer id;
    private String street;
    private String line;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private Float latitude;
    private Float longitude;

    public Address() {}
}
