package com.rideshare.userinfo.webentity;

import lombok.Data;

@Data
public class Place {
    private String name;
    private Integer userId;
    private Integer addressId;

    public Place() {}

    public Place(String name, Integer userId, Integer addressId) {
        this.name = name;
        this.userId = userId;
        this.addressId = addressId;
    }
}
