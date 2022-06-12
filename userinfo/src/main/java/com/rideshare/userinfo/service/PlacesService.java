package com.rideshare.userinfo.service;

import com.rideshare.userinfo.mapper.PlacesMapper;
import com.rideshare.userinfo.model.Place;
import com.rideshare.userinfo.model.UserInfo;
import com.rideshare.userinfo.util.Pagination;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacesService implements IPlacesService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public PaginatedEntity<Place> getAllPaginated(Integer userId, Integer page, Integer limit) throws Exception {
        Integer offset = Pagination.getOffset(page,limit);
        String query = "SELECT * FROM \"userinfo\".\"places\" LIMIT ? OFFSET ?";
        List<Place> placeList = jdbcTemplate.query(query, new PlacesMapper(), limit, offset);
        return new PaginatedEntity<Place>(placeList, page, limit);
    }

    @Override
    public Place getById(Integer userId, Integer id) throws Exception {
        String query = "SELECT * FROM \"userinfo\".\"places\" WHERE id = ? AND user_id = ?";
        return jdbcTemplate.queryForObject(query, new PlacesMapper(), id, userId);
    }

    @Override
    public Place create(Place object) throws Exception {
        String query = "INSERT INTO \"userinfo\".\"places\"(name, first_line, second_line, city, state, country, zipcode, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        Integer id = jdbcTemplate.queryForObject(query, Integer.class, object.getName(), object.getFirstLine(), object.getSecondLine(), object.getCity(), object.getState(), object.getCountry(), object.getZipcode(), object.getUserId());

        Place createdPlace = getById(object.getUserId(), id);

        UserInfo user = userInfoService.getById(object.getUserId());
        createdPlace.setUser(user);

        return createdPlace;
    }

    @Override
    public Place update(Integer userId, Integer placeId, Place object) throws Exception {
        return null;
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        return false;
    }
}
