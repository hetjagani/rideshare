package com.rideshare.rating.service;

import com.rideshare.rating.model.Rating;

public interface IRatingService {
    Rating getById(Integer ratingId) throws Exception;
    Rating create(Rating rating) throws Exception;
    Rating getRatingById(Integer id) throws Exception;
}
