package com.rideshare.userinfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
public class Report {

    private int id;
    private int userId;
    private int reportedId;
    private String category;
    private String description;

    public Report(){}

    public Report(int id, int userId, int reportedId, String category, String description) {
        this.id = id;
        this.userId = userId;
        this.reportedId = reportedId;
        this.category = category;
        this.description = description;
    }

    public Report(int userId, int reportedId, String category, String description) {
        this.userId = userId;
        this.reportedId = reportedId;
        this.category = category;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", userId=" + userId +
                ", reportedId=" + reportedId +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
