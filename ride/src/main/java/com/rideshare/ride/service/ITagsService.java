package com.rideshare.ride.service;

import com.rideshare.ride.model.Tag;
import com.rideshare.ride.webentity.PaginatedEntity;

import java.util.List;

public interface ITagsService {
    PaginatedEntity<Tag> searchTags(String query, Integer page, Integer limit) throws Exception;
    Tag create(Tag tag) throws Exception;
    boolean delete(Integer id) throws Exception;
}
