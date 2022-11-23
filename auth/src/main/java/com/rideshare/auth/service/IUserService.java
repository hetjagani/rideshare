package com.rideshare.auth.service;

import com.rideshare.auth.model.User;
import org.springframework.dao.DataAccessException;

import javax.xml.crypto.Data;
import java.util.List;

public interface IUserService {
    List<User> getAllUsers() throws DataAccessException;
    List<User> getAllUsersPaginated(Integer page, Integer limit) throws DataAccessException;
    User getUserById(Integer id) throws DataAccessException;
    User getUserByEmail(String email) throws DataAccessException;
    User createUser(User user) throws DataAccessException;
    List<String> getRoles(Integer id) throws DataAccessException;
    User updateRoles(Integer id, List<String> roles) throws DataAccessException;
}
