package com.rideshare.userinfo.mapper;

import com.rideshare.userinfo.model.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoMapper implements RowMapper<UserInfo> {
    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserInfo u = new UserInfo(rs.getString("first_name"), rs.getString("last_name"), rs.getString("profile_image"));
        u.setId(rs.getInt("id"));
        return u;
    }
}
