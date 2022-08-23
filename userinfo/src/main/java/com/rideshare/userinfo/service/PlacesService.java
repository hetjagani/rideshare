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
        String query = "SELECT * FROM \"userinfo\".\"places\" WHERE user_id = ? LIMIT ? OFFSET ?";
        List<Place> placeList = jdbcTemplate.query(query, new PlacesMapper(), userId, limit, offset);
        return new PaginatedEntity<Place>(placeList, page, limit);
    }

    @Override
    public Place getById(Integer userId, Integer id) throws Exception {
        String query = "SELECT * FROM \"userinfo\".\"places\" WHERE id = ? AND user_id = ?";
        Place place = jdbcTemplate.queryForObject(query, new PlacesMapper(), id, userId);

        UserInfo user = userInfoService.getById(userId);
        place.setUser(user);

        return place;
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
        String query = "UPDATE \"userinfo\".\"places\"\n" +
                "SET name=?, first_line=?, second_line=?, city=?, state=?, country=?, zipcode=?\n" +
                "WHERE id=? AND user_id=?;";
        jdbcTemplate.update(query, object.getName(), object.getFirstLine(), object.getSecondLine(), object.getCity(), object.getState(), object.getCountry(), object.getZipcode(), placeId, userId);

        Place updatedPlace = getById(userId, placeId);

        UserInfo user = userInfoService.getById(userId);
        updatedPlace.setUser(user);

        return updatedPlace;
    }

    @Override
    public boolean delete(Integer userId, Integer placeId) throws Exception {
        String query = "DELETE FROM userinfo.places\n" +
                "\tWHERE id=? AND user_id=?;";

        jdbcTemplate.update(query, placeId, userId);
        return true;
    }
}
