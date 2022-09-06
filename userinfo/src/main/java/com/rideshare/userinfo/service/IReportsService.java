package com.rideshare.userinfo.service;

import com.rideshare.userinfo.model.Report;
import com.rideshare.userinfo.webentity.PaginatedEntity;

public interface IReportsService {
    PaginatedEntity<Report> getAllPaginated(String token, Integer page, Integer limit) throws Exception;
    Report getById(Integer id) throws Exception;
    Report create(Report object) throws Exception;
    Report update(Report object) throws Exception;
    boolean delete(Integer id) throws Exception;
}
