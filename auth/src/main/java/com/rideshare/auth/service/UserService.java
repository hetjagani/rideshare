package com.rideshare.auth.service;

import com.rideshare.auth.mapper.RoleMapper;
import com.rideshare.auth.mapper.UserMapper;
import com.rideshare.auth.model.User;
import com.rideshare.auth.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers(Integer page, Integer limit) throws DataAccessException {
        int offset = Pagination.getOffset(page, limit);
        String sql = "SELECT * FROM \"auth\".\"user\" LIMIT ? OFFSET ?;";
        List<User> userList = jdbcTemplate.query(sql, new UserMapper(), new Object[]{limit, offset});

        return userList;
    }

    @Override
    public User getUserById(Integer id) throws DataAccessException {
        String sql = "SELECT * FROM \"auth\".\"user\" WHERE id=?;";
        User user = jdbcTemplate.queryForObject(sql, new UserMapper(), new Object[]{id});
        List<String> roles = getRoles(user.getId());
        user.setRoles(roles);
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws DataAccessException {
        String sql = "SELECT * FROM \"auth\".\"user\" WHERE email=?;";
        User user = jdbcTemplate.queryForObject(sql, new UserMapper(), new Object[]{email});
        List<String> roles = getRoles(user.getId());
        user.setRoles(roles);
        return user;
    }

    @Override
    public User createUser(User user) throws DataAccessException{
        String createSQL = "INSERT INTO \"auth\".\"user\"(email, password, phone_number, is_verified) VALUES(?,?,?,?) RETURNING id";
        Integer id = jdbcTemplate.queryForObject(createSQL, Integer.class, user.getEmail(), user.getPassword(), user.getPhoneNo(), user.getVerified());

        for (String role:
             user.getRoles()) {
           String createRoleSQL = "INSERT INTO \"auth\".\"role\"(user_id, role) VALUES(?,?);";
           jdbcTemplate.update(createRoleSQL, id, role.toUpperCase());
        }

        return getUserById(id);
    }

    @Override
    public List<String> getRoles(Integer id) throws DataAccessException {
        String sql = "SELECT role FROM \"auth\".\"role\" WHERE user_id=?;";
        List<String> roles = jdbcTemplate.query(sql, new RoleMapper(), new Object[]{id});
        return roles;
    }
}
