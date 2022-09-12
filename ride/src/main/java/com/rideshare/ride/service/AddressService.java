package com.rideshare.ride.service;

import com.rideshare.ride.mapper.AddressMapper;
import com.rideshare.ride.model.Address;
import com.rideshare.ride.util.Pagination;
import com.rideshare.ride.webentity.PaginatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService implements IAddressService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String getByIdQuery = "SELECT * FROM ride.address WHERE id = ?";
    private final String searchQuery = "SELECT * \n" +
            "FROM ride.address \n" +
            "WHERE street LIKE ? OR line LIKE ? OR city LIKE ? OR state LIKE ? OR country LIKE ? OR zipcode LIKE ?\n" +
            "LIMIT ? OFFSET ?";
    private final String insertQuery = "INSERT INTO ride.address(street, line, city, state, country, zipcode, lat, long)\n" +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

    private final String deleteQuery = "DELETE FROM ride.address WHERE id = ?";

    @Override
    public Address getById(Integer id) throws Exception {
        return jdbcTemplate.queryForObject(getByIdQuery, new AddressMapper(), id);
    }

    @Override
    public Address getOrCreate(Address address) throws Exception {
        if (address.getId() != null) {
            Address dbAddress = jdbcTemplate.queryForObject(getByIdQuery, new AddressMapper(), address.getId());

            if(dbAddress != null) {
                return dbAddress;
            } else {
                return create(address);
            }
        } else {
            return create(address);
        }
    }

    @Override
    public PaginatedEntity<Address> searchAddress(String query, Integer page, Integer limit) throws Exception {
        Integer offset = Pagination.getOffset(page, limit);
        if (query == null) query = "";
        query = "%" + query + "%";
        List<Address> searchedAddresses = jdbcTemplate.query(searchQuery, new AddressMapper(), query, query, query, query, query, query, limit, offset);
        return new PaginatedEntity<>(searchedAddresses, page, limit);
    }

    @Override
    public Address create(Address address) throws Exception {
        Integer id = jdbcTemplate.queryForObject(insertQuery, Integer.class, address.getStreet(), address.getLine(), address.getCity(), address.getState(), address.getCountry(), address.getZipcode(), address.getLatitude(), address.getLongitude());
        Address createdAddress = getById(id);
        return createdAddress;
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        int affectedRows = jdbcTemplate.update(deleteQuery, id);
        return affectedRows != 0;
    }
}
