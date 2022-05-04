package com.rideshare.auth.service;

import com.rideshare.auth.model.City;

import java.util.List;

public interface ICityService {
    List<City> findAll();
    City findById(Long id);
}
