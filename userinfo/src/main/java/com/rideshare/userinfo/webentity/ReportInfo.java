package com.rideshare.userinfo.webentity;

import lombok.Data;

@Data
public class ReportInfo {
    private int userId;
    private int reportedId;
    private String title;
    private String category;
    private String description;

    public ReportInfo(){}

    public ReportInfo(int userId, int reportedId, String title, String category, String description) {
        this.userId = userId;
        this.reportedId = reportedId;
        this.title = title;
        this.category = category;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Report{" +
                ", userId=" + userId +
                ", reportedId=" + reportedId +
                ", title=" + title +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
