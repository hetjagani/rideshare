package com.rideshare.userinfo.webentity;

import com.rideshare.userinfo.webentity.UserInfo;
import lombok.Data;

@Data
public class DetailedReport {
    private int reportId;
    private UserInfo userInfo;
    private UserInfo reportedUserInfo;
    private String title;
    private String category;
    private String description;

    public DetailedReport(){}

    public DetailedReport(UserInfo userInfo, UserInfo reportedUserInfo, String title, String category, String description) {
        this.userInfo = userInfo;
        this.reportedUserInfo = reportedUserInfo;
        this.title = title;
        this.category = category;
        this.description = description;
    }

    @Override
    public String toString() {
        return "DetailedReport{" +
                "userInfo=" + userInfo +
                ", reportedUserInfo=" + reportedUserInfo +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
