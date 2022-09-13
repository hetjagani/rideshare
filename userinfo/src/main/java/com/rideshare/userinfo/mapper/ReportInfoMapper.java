package com.rideshare.userinfo.mapper;

import com.rideshare.userinfo.webentity.ReportInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportInfoMapper implements RowMapper<ReportInfo> {
    @Override
    public ReportInfo mapRow(ResultSet rs, int rowNum) throws SQLException{
        ReportInfo reportInfo = new ReportInfo(rs.getInt("user_id"), rs.getInt("reported_id"), rs.getString("title"),
                rs.getString("category"), rs.getString("description"));
        return reportInfo;
    }
}