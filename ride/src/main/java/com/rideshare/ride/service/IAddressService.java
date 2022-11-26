package com.rideshare.ride.service;

import com.rideshare.ride.model.Address;
import com.rideshare.ride.webentity.PaginatedEntity;

public interface IAddressService {
    Address getById(Integer id) throws Exception;
    Address getOrCreate(Address address) throws Exception;
    PaginatedEntity<Address> searchAddress(String query, Integer page, Integer limit) throws Exception;
    Address searchOneAddress(Float lat, Float lon) throws Exception;
    Address create(Address address) throws Exception;
    boolean delete(Integer id) throws Exception;
}
