package com.rideshare.rating.service;

import com.rideshare.rating.model.Rating;

public interface IRatingService {
    Rating getById(Integer ratingId) throws Exception;
    Rating create(Rating rating, String token) throws Exception;
    com.rideshare.rating.webentity.Rating getRatingById(Integer id, String token) throws Exception;
}
