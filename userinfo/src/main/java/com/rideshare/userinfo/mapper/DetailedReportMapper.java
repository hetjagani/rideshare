package com.rideshare.userinfo.mapper;

import com.rideshare.userinfo.webentity.DetailedReport;
import com.rideshare.userinfo.webentity.UserInfo;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class DetailedReportMapper implements RowMapper<DetailedReport> {
    @Override
    public DetailedReport mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(rs.getInt(7));
        userInfo.setFirstName(rs.getString(8));
        userInfo.setLastName(rs.getString(9));
        userInfo.setProfileImage(rs.getString(10));

        UserInfo reportedInfo = new UserInfo();
        reportedInfo.setId(rs.getInt(11));
        reportedInfo.setFirstName(rs.getString(12));
        reportedInfo.setLastName(rs.getString(13));
        reportedInfo.setProfileImage(rs.getString(14));

        DetailedReport detailedReport = new DetailedReport();
        detailedReport.setReportId(rs.getInt(1));
        detailedReport.setUserInfo(userInfo);
        detailedReport.setReportedUserInfo(reportedInfo);

        detailedReport.setTitle(rs.getString("title"));
        detailedReport.setCategory(rs.getString("category"));
        detailedReport.setDescription(rs.getString("description"));

        return detailedReport;
    }
}
