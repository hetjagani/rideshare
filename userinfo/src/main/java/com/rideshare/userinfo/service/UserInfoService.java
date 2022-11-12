package com.rideshare.userinfo.service;

import com.rideshare.userinfo.mapper.UserInfoMapper;
import com.rideshare.userinfo.model.User;
import com.rideshare.userinfo.model.UserInfo;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import com.rideshare.userinfo.webentity.Ride;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserInfoService implements IUserInfoService {

    private final Logger logger = LogManager.getLogger(UserInfoService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.auth.url}")
    private String authURL;

    @Override
    public PaginatedEntity<UserInfo> getAllPaginated(String token, Integer page, Integer limit) throws Exception {
        // get list of users from auth service with pagination params
        String requestURL = authURL + "/auth/users" + "?page={page}&limit={limit}";
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", page);
        queryParams.put("limit", limit);

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", token);

        HttpEntity request = new HttpEntity(header);

        ResponseEntity<User[]> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, request, User[].class, queryParams);
        List<User> userList = Arrays.asList(responseEntity.getBody());
        List<Integer> userIds = userList.stream().map((u) -> u.getId()).collect(Collectors.toList());

        Map<Integer, User> userMap = new HashMap<Integer, User>();
        userList.forEach((u) -> {
            userMap.put(u.getId(), u);
        });

        // fetch userinfo of all those user ids
        String inParams = String.join(",", userIds.stream().map(id -> "?").collect(Collectors.toList()));

        String userInfoSQL = String.format("SELECT * FROM \"userinfo\".\"userinfo\" WHERE id IN (%s);", inParams);

        List<UserInfo> userInfoList = jdbcTemplate.query(userInfoSQL, new UserInfoMapper(), userIds.toArray());

        List<UserInfo> result = userInfoList.stream().map((u) -> {
            u.setEmail(userMap.get(u.getId()).getEmail());
            u.setPhoneNo(userMap.get(u.getId()).getPhoneNo());
            u.setRoles(userMap.get(u.getId()).getRoles());
            u.setVerified(userMap.get(u.getId()).getVerified());
            return u;
        }).collect(Collectors.toList());

        return new PaginatedEntity<UserInfo>(result, page != null ? page : 0, limit);
    }

    @Override
    public UserInfo getById(Integer id) throws Exception {
        String sql = "SELECT * FROM \"userinfo\".\"userinfo\" WHERE id = ?;";

        return jdbcTemplate.queryForObject(sql, new UserInfoMapper(), id);
    }

    @Override
    public UserInfo create(UserInfo object) throws Exception {
        // create user info object in the database and return the original object with all info
        String createSQL = "INSERT INTO \"userinfo\".\"userinfo\"(id, first_name, last_name, profile_image, created_at) VALUES(?,?,?,?,?) RETURNING id";
        Integer createdId = jdbcTemplate.queryForObject(createSQL, Integer.class, object.getId(), object.getFirstName(), object.getLastName(), object.getProfileImage(), Date.from(Instant.now()));
        return getById(createdId);
    }

    @Override
    public UserInfo update(UserInfo object) throws Exception {
        // update user info object in the database and return the original object with all info
        String updateSQL = "UPDATE \"userinfo\".\"userinfo\" SET first_name=?, last_name=?, profile_image=? WHERE id=?";
        jdbcTemplate.update(updateSQL, object.getFirstName(), object.getLastName(), object.getProfileImage(), object.getId());
        return getById(object.getId());
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        return false;
    }
}
