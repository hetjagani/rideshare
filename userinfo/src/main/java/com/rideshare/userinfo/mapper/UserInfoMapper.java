package com.rideshare.userinfo.mapper;

import com.rideshare.userinfo.model.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;

public class UserInfoMapper implements RowMapper<UserInfo> {
    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        Date createdAt = rs.getDate("created_at");
        long months = createdAt.toLocalDate().until(LocalDate.now(), ChronoUnit.MONTHS);
        UserInfo u = new UserInfo(rs.getString("first_name"), rs.getString("last_name"), rs.getString("profile_image"), rs.getDate("created_at"), (int) months);
        u.setId(rs.getInt("id"));
        return u;
    }
}
