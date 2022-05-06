package com.rideshare.auth.service;

import com.rideshare.auth.model.User;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IUserService {
    User getUserById(Integer id) throws DataAccessException;
    User getUserByEmail(String email) throws DataAccessException;
    List<User> getAllUsers(Integer id) throws DataAccessException;
    User createUser(User user) throws DataAccessException;

    List<String> getRoles(Integer id) throws DataAccessException;
}
