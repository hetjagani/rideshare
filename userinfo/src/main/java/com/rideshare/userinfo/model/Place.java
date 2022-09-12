package com.rideshare.userinfo.model;

import lombok.Data;

@Data
public class Place {
    private Integer id;
    private String name;
    private Integer userId;
    private Integer addressId;

    public Place() {}

    public Place(Integer id, String name, Integer userId, Integer addressId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.addressId = addressId;
    }

    public Place(String name, Integer userId, Integer addressId) {
        this.name = name;
        this.userId = userId;
        this.addressId = addressId;
    }
}
