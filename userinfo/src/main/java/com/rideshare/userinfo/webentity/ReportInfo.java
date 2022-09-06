package com.rideshare.userinfo.webentity;

import lombok.Data;

@Data
public class ReportInfo {
    private int reportedId;
    private String category;
    private String description;

    public ReportInfo(){}

    public ReportInfo(int reportedId, String category, String description) {
        this.reportedId = reportedId;
        this.category = category;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Report{" +
                ", reportedId=" + reportedId +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
