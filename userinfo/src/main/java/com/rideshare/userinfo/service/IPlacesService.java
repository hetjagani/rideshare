package com.rideshare.userinfo.service;

import com.rideshare.userinfo.model.Place;
import com.rideshare.userinfo.webentity.PaginatedEntity;

public interface IPlacesService {
    PaginatedEntity<Place> getAllPaginated(Integer userId, Integer page, Integer limit) throws Exception;
    Place getById(Integer userId, Integer id) throws Exception;
    Place create(Place object) throws Exception;
    Place update(Integer userId, Integer placeId, Place object) throws Exception;
    boolean delete(Integer userId, Integer placeId) throws Exception;
}
