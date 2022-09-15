package com.rideshare.rating.service;

import com.rideshare.rating.model.Rating;

public interface IRatingService {
    Rating create(Rating rating) throws Exception;
    Rating getRatingById(Integer id) throws Exception;
}
