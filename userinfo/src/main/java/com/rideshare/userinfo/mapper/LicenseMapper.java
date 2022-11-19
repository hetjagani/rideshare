package com.rideshare.userinfo.mapper;

import com.rideshare.userinfo.model.License;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LicenseMapper implements RowMapper<License> {

    @Override
    public License mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new License(rs.getInt("id"), rs.getInt("user_id"), rs.getString("license_number"), rs.getString("first_name"), rs.getString("middle_name"), rs.getString("last_name"), rs.getString("sex"), rs.getString("height"), rs.getString("weight"), rs.getString("dob"), rs.getString("expiry"), rs.getString("issued"), rs.getString("address1"), rs.getString("address2"), rs.getString("postcode"), rs.getString("issuer_region"), rs.getString("issuer_org"), rs.getBoolean("verification"));
    }
}
