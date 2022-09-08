package com.rideshare.ride.service;

import com.rideshare.ride.model.Address;
import com.rideshare.ride.webentity.PaginatedEntity;

public interface IAddressService {
    PaginatedEntity<Address> searchAddress(String query, Integer page, Integer limit) throws Exception;
    Address create(Address address) throws Exception;
    boolean delete(Integer id) throws Exception;
}
