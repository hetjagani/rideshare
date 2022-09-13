package com.rideshare.userinfo.service;

import java.util.List;

public interface DAOInterface<T> {
    List<T> getAll() throws Exception;
    List<T> getAllPaginated(Integer page, Integer limit) throws Exception;

    T getById(Integer id) throws Exception;
    T create(T object) throws Exception;
    T update(T object) throws Exception;
    boolean delete(Integer id) throws Exception;
}
