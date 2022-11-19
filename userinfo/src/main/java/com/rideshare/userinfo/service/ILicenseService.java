package com.rideshare.userinfo.service;

import com.rideshare.userinfo.model.License;
import com.rideshare.userinfo.webentity.PaginatedEntity;

public interface ILicenseService {
    PaginatedEntity<License> getAllPaginated(Integer page, Integer limit) throws Exception;
    License getById(Integer id) throws Exception;
    License getByUserId(Integer userId) throws Exception;
    License create(License license) throws Exception;
    boolean delete(Integer id) throws Exception;
}
