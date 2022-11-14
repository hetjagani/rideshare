package com.rideshare.rating.service;

import com.rideshare.rating.model.Rating;
import com.rideshare.rating.webentity.PaginatedEntity;

public interface IRatingService {
    com.rideshare.rating.webentity.Rating create(Rating rating, String token) throws Exception;
    com.rideshare.rating.webentity.Rating getRatingById(Integer id, String token) throws Exception;

    PaginatedEntity<com.rideshare.rating.webentity.Rating> getAllRatings(String token, Integer page, Integer limit, Integer userId,
                                                                         Integer ratingUserId, Boolean all) throws Exception;
    Boolean delete(Integer userId, Integer id) throws Exception;
    Float getAvgRating(Integer userId) throws Exception;
}
