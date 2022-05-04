package com.rideshare.auth.service;

import com.rideshare.auth.mapper.CityMapper;
import com.rideshare.auth.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService implements ICityService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<City> findAll() {
        return jdbcTemplate.query("SELECT * FROM cities;", new CityMapper());
    }

    @Override
    public City findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM cities WHERE id=?",new CityMapper(), id);
    }
}
