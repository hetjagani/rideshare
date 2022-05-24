package com.rideshare.auth.service;

import com.rideshare.auth.model.User;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers(Integer page, Integer limit) throws DataAccessException;
    User getUserById(Integer id) throws DataAccessException;
    User getUserByEmail(String email) throws DataAccessException;
    User createUser(User user) throws DataAccessException;
    List<String> getRoles(Integer id) throws DataAccessException;
}
