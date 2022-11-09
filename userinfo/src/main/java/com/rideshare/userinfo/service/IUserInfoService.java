package com.rideshare.userinfo.service;

import com.rideshare.userinfo.model.UserInfo;
import com.rideshare.userinfo.webentity.PaginatedEntity;

public interface IUserInfoService {
    PaginatedEntity<UserInfo> getAllPaginated(String token, Integer page, Integer limit) throws Exception;
    UserInfo getById(Integer id, String token) throws Exception;
    UserInfo create(UserInfo object) throws Exception;
    UserInfo update(UserInfo object) throws Exception;
    boolean delete(Integer id) throws Exception;
}
